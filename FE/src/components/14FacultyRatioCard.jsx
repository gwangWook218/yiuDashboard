import React, { useMemo, useState, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  BarElement,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, Tooltip, Legend, BarElement);

const BRAND = '#028EA7';
const BRAND_SOFT = 'rgba(2, 142, 167, 0.6)';

const diffArrow = (d) => (d > 0 ? "▲" : d < 0 ? "▼" : "—");
const diffClass = (d) =>
  d > 0 ? "text-rose-600" : d < 0 ? "text-[#028EA7]" : "text-slate-500";

// 🚀 [수정] YEARS를 2022, 2023, 2024로 되돌립니다.
const YEARS = ['2022', '2023', '2024']; 

export default function FacultyStudentRatio() {
  const [yiuData, setYiuData] = useState({});
  const [regionData, setRegionData] = useState([]);
  
  // 🚀 [수정] 현재 년도를 2024, 이전 년도를 2023으로 되돌립니다.
  const year = '2024';
  const prevYear = '2023';

  // ✅ API 연동
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [compareRes, regionRes] = await Promise.all([
          fetch('http://ec2-13-209-7-237.ap-northeast-2.compute.amazonaws.com:8080/api/admin/student-ratio/compare'),
          fetch('http://ec2-13-209-7-237.ap-northeast-2.compute.amazonaws.com:8080/api/admin/student-ratio/region')
        ]);

        const compareJson = await compareRes.json();
        const regionJson = await regionRes.json();

        console.log("✅ compareJson:", compareJson);
        console.log("✅ regionJson:", regionJson);

        // 용인대 데이터 매핑 (년도: 값)
        const yiuObj = {};
        compareJson.forEach((d) => {
          yiuObj[d.year] = d.value;
        });
        // 용인대 데이터에 대한 추가적인 2025 임시값 설정은 제거되었습니다.

        // 지역 데이터 매핑
        const grouped = ['전체', '수도권', '비수도권'].map(region => {
          const ratio = regionJson
            .filter(r => r.region === region)
            .reduce((acc, cur) => ({ ...acc, [cur.year]: cur.value }), {});
            
          // 🚀 [핵심 수정] 지역 데이터의 2022년도 자리에 2025년도 데이터를 사용합니다.
          if (ratio['2025'] !== undefined) {
              ratio['2022'] = ratio['2025'];
          }
          // 2025년 데이터는 이제 2022년 키에 들어갔으므로, 2025년 키는 삭제할 필요는 없지만,
          // YEARS 배열에 2025가 없으므로 차트/테이블에는 표시되지 않습니다.

          return { region, ratio };
        });

        setYiuData(yiuObj);
        setRegionData(grouped);
      } catch (err) {
        console.error("❌ API fetch error:", err);
      }
    };
    fetchData();
  }, []);

  // ✅ 데이터 준비 (기존 구조 그대로)
  const current = yiuData[year];
  const prev = yiuData[prevYear];
  const yoy = prev ? +(current - prev).toFixed(1) : null;

  const getRatio = (region, y) =>
    regionData.find(r => r.region === region)?.ratio?.[y] ?? null;

  const seoul = getRatio('수도권', year);
  const nonSeoul = getRatio('비수도권', year);
  const vsSeoul = seoul ? +(current - seoul).toFixed(1) : null;

  // 🚀 vsSeoul 결과에 따라 텍스트를 JSX로 바로 구성합니다.
  const vsSeoulText = useMemo(() => {
    if (vsSeoul === null) {
      return '수도권 평균 데이터 없음';
    }
    
    const statusText = vsSeoul >= 0 ? '높습니다' : '낮습니다';
    const statusClass = vsSeoul >= 0 ? 'text-rose-600' : 'text-[#028EA7]';

    return (
      <div className="mt-1 text-xs font-semibold">
        용인대 1인당 학생 수는 수도권 평균보다
        <span className={statusClass}>
          {` ${statusText}`}
        </span>
      </div>
    );
  }, [vsSeoul]);


  // ✅ Bar Chart
  const barData = useMemo(() => {
    const labels = YEARS;
    const regions = ['용인대학교', '수도권', '비수도권', '전국 평균'];

    const BAR_COLORS = {
      '용인대학교': { bg: BRAND_SOFT, border: BRAND },
      '수도권': { bg: 'rgba(255, 193, 7, 0.4)', border: '#FFC107' },
      '비수도권': { bg: 'rgba(23, 162, 184, 0.4)', border: '#17A2B8' },
      '전국 평균': { bg: 'rgba(100,116,139,0.4)', border: '#64748B' },
    };

    const datasets = regions.map(regionName => {
      let ratioData = [];

      if (regionName === '용인대학교') {
        ratioData = YEARS.map(y => yiuData[y] ?? 0);
      } else {
        const regionKey = regionName === '전국 평균' ? '전체' : regionName;
        const region = regionData.find(r => r.region === regionKey);
        // 지역 데이터는 2022 키에 2025 값이 들어가있으므로 그대로 YEARS를 사용
        ratioData = YEARS.map(y => region?.ratio?.[y] ?? 0); 
      }

      const color = BAR_COLORS[regionName];
      return {
        label: regionName,
        data: ratioData,
        backgroundColor: color.bg,
        borderColor: color.border,
        borderWidth: 1.5,
        borderRadius: 8,
      };
    });

    return { labels, datasets };
  }, [regionData, yiuData]);

  const barOptions = useMemo(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { position: 'top', labels: { usePointStyle: true } },
      tooltip: {
        callbacks: {
          label: (ctx) => `${ctx.dataset.label} (${ctx.label}): ${ctx.raw.toFixed(1)}명`
        }
      }
    },
    scales: {
      x: {
        grid: { display: false },
        categoryPercentage: 0.85,
        barPercentage: 0.4,
      },
      y: {
        beginAtZero: false,
        title: { display: true, text: '명' }
      }
    }
  }), []);

  // ✅ 표 데이터 구성
  const RatioComparisonDataSources = useMemo(() => [
    { label: '용인대', type: 'yiu', data: yiuData },
    { label: '수도권', type: 'region', data: regionData.find(r => r.region === '수도권')?.ratio ?? {} },
    { label: '비수도권', type: 'region', data: regionData.find(r => r.region === '비수도권')?.ratio ?? {} },
    { label: '전국 평균', type: 'region', data: regionData.find(r => r.region === '전체')?.ratio ?? {} },
  ], [regionData, yiuData]);

  const RatioDifferences = useMemo(() =>
    RatioComparisonDataSources.map(s => {
      const rawDCurrent = s.data[year];
      const rawDPrev = s.data[prevYear];
      
      if (!rawDPrev || !rawDCurrent) return null;

      const dCurrent = Math.round(rawDCurrent * 10) / 10;
      const dPrev = Math.round(rawDPrev * 10) / 10;
      
      const diff = dCurrent - dPrev;
      
      return Math.round(diff * 10) / 10;
    }), [RatioComparisonDataSources, year, prevYear]
  );

  const RatioComparisonTable = () => (
    <div className="rounded-2xl shadow border border-gray-200 p-4 h-full">
      <h3 className="text-base font-semibold mb-1">전임교원 1인당 학생 수 현황 - 지역 비교 (명)</h3>
      <div className="overflow-x-auto pt-4 mb-2">
        <table className="text-sm w-full border-collapse">
          <thead>
            <tr className="text-gray-700 bg-slate-50 border-b border-gray-200">
              <th className="py-3 px-3 text-left w-40 font-semibold">연도/구분</th>
              {RatioComparisonDataSources.map(s => (
                <th key={s.label} className="py-3 px-3 text-center font-semibold text-gray-700">{s.label}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {YEARS.map((y) => (
              <tr key={y} className="border-b border-gray-200 hover:bg-slate-50">
                <td className={`py-3 px-3 font-medium text-left bg-slate-50/50 text-slate-800 ${y === year ? 'font-bold' : ''}`}>
                    {/* 🚀 [변경] 2022년은 '2022 (2025 값)'으로 표시하여 2025년 값이 사용됨을 명시 */}
                    {y === '2022' && RatioComparisonDataSources.some(s => s.type === 'region' && s.data[y] !== undefined) ? '2022' : y}
                </td>
                {RatioComparisonDataSources.map((s) => {
                  const val = s.data[y];
                  const displayValue = val ? `${val.toFixed(1)}명` : '—';
                  const isYIU = s.type === 'yiu';
                  const cellClass = `${isYIU ? 'bg-cyan-50/50 font-medium' : ''} ${y === year ? 'font-bold' : ''}`;
                  return (
                    <td key={`${s.label}-${y}`} className={`py-3 text-center text-slate-700 ${cellClass}`}>{displayValue}</td>
                  );
                })}
              </tr>
            ))}
            <tr className="border-b border-gray-200 last:border-b-0 transition-colors bg-white hover:bg-slate-50">
              <td className="py-3 px-3 font-bold text-left bg-slate-50/50 text-slate-800">증감 ({prevYear} → {year})</td>
              {RatioDifferences.map((d, idx) => (
                <td key={`diff-${idx}`} className={`py-3 text-center font-bold ${d === null ? 'text-slate-500' : diffClass(d)} ${RatioComparisonDataSources[idx].type === 'yiu' ? 'bg-cyan-50' : ''}`}>
                  {d === null ? '—' : `${diffArrow(d)} ${Math.abs(d).toFixed(1)}명`}
                </td>
              ))}
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  );

  return (
    <div className="w-full h-full mx-auto p-6 bg-white rounded-2xl shadow">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-xl font-semibold">전임교원 1인당 학생 수</h2>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-3 mb-5">
        {/* 1️⃣ 용인대 카드 */}
        <div className="rounded-xl shadow border border-gray-200 p-4">
          <div className="text-sm font-bold text-gray-500">용인대학교 ({year})</div>
          <div className="text-3xl font-bold text-[#0f172a]">{current?.toFixed(1)}<span className="text-base text-gray-500 ml-1">명</span></div>
          <div className={`mt-1 inline-flex items-center gap-1 text-xs rounded-full px-2 py-0.5 ${yoy === null ? 'bg-slate-50 text-slate-500' : (yoy >= 0 ? 'bg-rose-50 text-rose-600' : 'bg-emerald-50 text-[#028EA7]')}`}>
            {yoy === null ? '— 전년 없음' : `${yoy >= 0 ? '▲' : '▼'} ${Math.abs(yoy).toFixed(1)}명 (전년 대비)`}
          </div>
        </div>

        {/* 2️⃣ 수도권 / 비수도권 */}
        <div className="rounded-xl shadow border border-gray-200 p-4">
          <div className="text-sm font-bold text-gray-500">수도권 / 비수도권 ({year})</div>
          <div className="flex items-end gap-5 mt-1">
            <div>
              <div className="text-xs text-gray-500 mb-0.5">수도권</div>
              <div className="text-xl font-semibold">{seoul?.toFixed(1)}<span className="text-sm text-gray-500 ml-1">명</span></div>
            </div>
            <div>
              <div className="text-xs text-gray-500 mb-0.5">비수도권</div>
              <div className="text-xl font-semibold">{nonSeoul?.toFixed(1)}<span className="text-sm text-gray-500 ml-1">명</span></div>
            </div>
          </div>
        </div>

        {/* 3️⃣ 용인대 vs 수도권 */}
        <div className="rounded-xl shadow border border-gray-200 p-4">
          <div className="text-sm font-bold text-gray-500">용인대 vs 수도권 ({year})</div>
          <div className={`text-3xl font-bold mt-1 ${vsSeoul === null ? 'text-slate-500' : (vsSeoul >= 0 ? 'text-rose-600' : 'text-[#028EA7]')}`}>
            {vsSeoul === null ? '—' : (vsSeoul >= 0 ? `+${vsSeoul}` : vsSeoul)}<span className="text-base font-normal ml-1">{vsSeoul === null ? '' : '명'}</span>
          </div>
          {vsSeoulText}
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mb-6">
        <RatioComparisonTable />
        <div className="rounded-2xl shadow border border-gray-200 p-4">
          <div className="h-[260px]">
            <Bar data={barData} options={barOptions} />
          </div>
        </div>
      </div>

      <div className="mt-6 mx-auto w-[200px] border-t border-gray-200" />
    </div>
  );
}