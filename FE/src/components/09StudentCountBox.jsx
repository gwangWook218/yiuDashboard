import React, { useMemo, useState, useEffect } from 'react';
import { Chart } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  LineElement,
  PointElement,
  Tooltip,
  Legend
);

const BRAND = '#028EA7';
const BASE_SCAN_YEAR = 2024;
const DEPT_CACHE = Object.create(null);

// ------------------------------------------------------------------
// API 호출 함수 (수정됨)
// ------------------------------------------------------------------
async function fetchCount(year) {
  const endpoint = `/api/public/main/department/students/count?year=${year}`;
  const response = await fetch(endpoint);
  if (!response.ok) throw new Error(`API Error: ${response.status}`);
  const data = await response.json();

  if (!Array.isArray(data)) {
    throw new Error(`Expected array but got ${typeof data}`);
  }
  return data; // 전체 학과 데이터 배열 반환
}

// ------------------------------------------------------------------
// 유효한 학과 목록 추출 (수정됨)
// ------------------------------------------------------------------
async function discoverDeptListByScanning(year) {
  if (DEPT_CACHE[year]) return DEPT_CACHE[year];

  const data = await fetchCount(year);

  const list = data
    .filter(item => typeof item.totalStudents === 'number' && item.department)
    .map((item, index) => ({
      id: index + 1, // 단순한 내부 인덱스
      name:
        item.isDaytime === '야간'
          ? `${item.department} (야간)`
          : item.department,
      isDaytime: item.isDaytime,
    }));

  DEPT_CACHE[year] = list;
  return list;
}

// ------------------------------------------------------------------
// 연도별 특정 학과 재학생 수 조회 (수정됨)
// ------------------------------------------------------------------
async function fetchEnrollmentForYear({ deptName, year }) {
  try {
    const data = await fetchCount(year);
    const isNight = deptName.includes('(야간)');
    const pureDept = deptName.replace(' (야간)', '');
    const row = data.find(
      item =>
        item.department === pureDept &&
        (isNight ? item.isDaytime === '야간' : item.isDaytime === '주간')
    );
    if (row && typeof row.totalStudents === 'number') {
      return {
        year: String(year),
        count: row.totalStudents,
        departmentName: row.department,
      };
    }
    return null;
  } catch (error) {
    return null;
  }
}

// ------------------------------------------------------------------
// 메인 컴포넌트
// ------------------------------------------------------------------
export default function GraduateEnrollmentByYear() {
  const [departmentId, setDepartmentId] = useState(null);
  const [departmentList, setDepartmentList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [listLoading, setListLoading] = useState(true);
  const [trend, setTrend] = useState([]);

  const selectedDeptName = useMemo(
    () =>
      departmentList.find((d) => d.id === Number(departmentId))?.name ||
      '학과 선택 필요',
    [departmentId, departmentList]
  );

  // ------------------------------------------------------
  // 1. 최초 학과 목록 로드
  // ------------------------------------------------------
   useEffect(() => {
    let mounted = true;
    async function loadDeptList() {
      setListLoading(true);
      try {
        const list = await discoverDeptListByScanning(BASE_SCAN_YEAR);

        if (mounted) {
          setDepartmentList(list);
          if (list.length > 0) {
            // 🎯🎯🎯 수정된 초기 학과 ID 설정 로직 🎯🎯🎯
            const initialDeptId = 36;
            const targetDept = list.find(d => d.id === initialDeptId);
            setDepartmentId(targetDept ? initialDeptId : list[0].id);
          }
        }
      } catch (error) {
        if (mounted) setDepartmentList([]);
      } finally {
        if (mounted) setListLoading(false);
      }
    }
    loadDeptList();
    return () => { mounted = false; };
  }, []);


  // ------------------------------------------------------
  // 2. 학과 선택 시 3개년 데이터 로드
  // ------------------------------------------------------
  useEffect(() => {
    if (!departmentId) return;
    let mounted = true;

    async function loadEnrollmentData() {
      setLoading(true);
      try {
        const yearsToFetch = ['2024', '2023', '2022'];
        const deptName =
          departmentList.find((d) => d.id === Number(departmentId))?.name;

        const results = await Promise.all(
          yearsToFetch.map((year) =>
            fetchEnrollmentForYear({ deptName, year })
          )
        );

        const validTrend = results
          .filter((t) => t !== null && t.count !== undefined)
          .sort((a, b) => a.year.localeCompare(b.year));

        if (mounted) setTrend(validTrend);
      } catch {
        if (mounted) setTrend([]);
      } finally {
        if (mounted) setLoading(false);
      }
    }

    loadEnrollmentData();
    return () => {
      mounted = false;
    };
  }, [departmentId, departmentList]);

  // ------------------------------------------------------
  // 3. 통계 계산
  // ------------------------------------------------------
  const stats = useMemo(() => {
    const years = trend.map((t) => t.year);
    const vals = trend.map((t) => t.count);
    if (vals.length === 0) return null;

    const current = vals[vals.length - 1];
    const prev = vals[vals.length - 2] ?? null;
    const yoy = prev !== null ? current - prev : null;
    const avg = Math.round(vals.reduce((a, b) => a + b, 0) / vals.length);
    const maxVal = Math.max(...vals);
    const minVal = Math.min(...vals);
    const maxYear = years[vals.indexOf(maxVal)];
    const minYear = years[vals.indexOf(minVal)];
    const yoyPct = prev ? ((yoy / prev) * 100).toFixed(1) : null;

    return { current, prev, yoy, yoyPct, avg, maxVal, minVal, maxYear, minYear };
  }, [trend]);

  // ------------------------------------------------------
  // 4. 차트 데이터
  // ------------------------------------------------------
  const chartData = useMemo(() => {
    const labels = trend?.map((t) => t.year) || ['2022', '2023', '2024'];
    const data = trend?.map((t) => t.count) || [0, 0, 0];
    return {
      labels,
      datasets: [
        {
          type: 'line',
          label: '추이',
          data,
          borderColor: 'rgba(2, 142, 167, 0.7)',
          borderWidth: 2,
          backgroundColor: 'transparent',
          fill: false,
          tension: 0,
          pointRadius: 6,
          pointBackgroundColor: '#fff',
          pointBorderColor: BRAND,
          pointBorderWidth: 2,
          borderDash: [5, 5],
        },
        {
          type: 'bar',
          label: '재학생 수',
          data,
          backgroundColor: 'rgba(142, 222, 230, 0.7)',
          borderRadius: 9,
          borderWidth: 0,
          barPercentage: 0.7,
          categoryPercentage: 0.5,
        },
      ],
    };
  }, [trend]);

  // ------------------------------------------------------
  // 5. JSX
  // ------------------------------------------------------
  return (
    <div className="p-4 w-full h-full mx-auto">
      <h2 className="text-xl font-semibold mb-3">재학생 수 상세 분석</h2>
      <hr className="my-3 border-t border-gray-200" />

      <div className="mb-4">
        {listLoading ? (
          <span className="ml-2 text-sm text-gray-500">학과 목록 로드 중...</span>
        ) : (
          <select
            value={departmentId || ''}
            onChange={(e) => setDepartmentId(Number(e.target.value))}
            className="border border-[#028EA7] font-semibold rounded-lg p-2 bg-white focus:outline-none focus:ring-2 focus:ring-[#028EA7]"
            disabled={departmentList.length === 0}
          >
            {departmentList.length === 0 ? (
              <option value="">학과 목록 없음</option>
            ) : (
              departmentList.map((dep) => (
                <option key={dep.id} value={dep.id}>
                  {dep.name}
                </option>
              ))
            )}
          </select>
        )}
        {loading && (
          <span className="ml-2 text-sm text-gray-500">데이터 불러오는 중…</span>
        )}
        {departmentList.length === 0 && !listLoading && (
          <span className="ml-2 text-sm text-rose-600">
            유효한 학과를 찾지 못했습니다.
          </span>
        )}
      </div>

      <div className="grid grid-cols-2 gap-3 mb-5">
        <div className="rounded-xl p-3 bg-gray-50">
          <div className="text-xs font-semibold text-gray-500">
            현재 재학생 수({trend[trend.length - 1]?.year || 'N/A'})
          </div>
          <div className="text-2xl font-semibold">
            {stats?.current ?? '-'}
            <span className="text-sm text-gray-500"> 명</span>
          </div>
        </div>
        <div className="rounded-xl p-3 bg-gray-50">
          <div className="text-xs font-semibold text-gray-500">전년 대비</div>
          <div
            className={`text-2xl font-semibold ${
              stats?.yoy > 0
                ? 'text-[#028EA7]'
                : stats?.yoy < 0
                ? 'text-rose-600'
                : ''
            }`}
          >
            {stats?.yoy === null
              ? '-'
              : `${stats?.yoy > 0 ? '▲' : stats?.yoy < 0 ? '▼' : '—'} ${Math.abs(
                  stats?.yoy
                )}명`}
            <span className="text-sm text-gray-500">
              {stats?.yoyPct ? ` (${stats?.yoyPct}%)` : ''}
            </span>
          </div>
        </div>
        <div className="rounded-xl p-3 bg-gray-50">
          <div className="text-xs font-semibold text-gray-500 ">3개년 평균</div>
          <div className="text-2xl font-semibold">
            {stats?.avg ?? '-'}
            <span className="text-sm text-gray-500"> 명</span>
          </div>
        </div>
        <div className="rounded-xl p-3 bg-gray-50">
          <div className="text-xs font-semibold text-gray-500">최고 / 최저</div>
          <div className="text-lg font-semibold flex flex-col">
            {stats ? (
              <>
                <span>{`${stats.maxYear} ${stats.maxVal}명`}</span>
                <span>{`${stats.minYear} ${stats.minVal}명`}</span>
              </>
            ) : (
              '-'
            )}
          </div>
        </div>
      </div>

      <div className="rounded-xl p-3 bg-white shadow-md h-[300px]">
        <h3 className="text-base font-semibold mb-3">
          {selectedDeptName} 3개년 추이
        </h3>
        <Chart
          type="bar"
          data={chartData}
          options={{
            responsive: true,
            maintainAspectRatio: false,
            layout: { padding: { bottom: 25 } },
            plugins: {
              legend: { display: false },
              tooltip: {
                callbacks: {
                  title: (items) => `연도: ${items?.[0]?.label ?? ''}`,
                  label: (ctx) => `${ctx.dataset.label}: ${ctx.parsed.y}명`,
                },
              },
            },
            scales: {
              y: {
                beginAtZero: true,
                title: { display: true, text: '재학생 수' },
              },
              x: { grid: { display: false } },
            },
          }}
        />
      </div>
    </div>
  );
}
