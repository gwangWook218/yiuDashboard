import React, { useState, useEffect, useMemo } from 'react';
import { Bar, Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Tooltip,
  Legend,
  ArcElement,
} from 'chart.js';

ChartJS.register(CategoryScale, LinearScale, BarElement, Tooltip, Legend, ArcElement);

const BRAND = '#028EA7';
const BRAND_SOFT = 'rgba(2, 142, 167, 0.6)';
const ALT = '#efe63bff';
const ALT_SOFT = 'rgba(221, 230, 123, 0.35)';

const YEARS = ['2021','2022','2023'];

async function fetchEmploymentData(year) {
  const res = await fetch(`/api/faculty/department/graduates/employment-rates?year=${year}`);
  if (!res.ok) throw new Error('취업 API 호출 실패');
  return res.json();
}

async function fetchAdmissionData(year) {
  const res = await fetch(`/api/faculty/department/graduates/admission-detail?year=${year}`);
  if (!res.ok) throw new Error('진학 API 호출 실패');
  return res.json();
}

export default function GraduateOutcomes() {
  const [selectedDept, setSelectedDept] = useState('컴퓨터과학과');
  const [departments, setDepartments] = useState([]);
  const [year, setYear] = useState('2023');
  const [employmentData, setEmploymentData] = useState(null);
  const [admissionData, setAdmissionData] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    let mounted = true;
    async function loadData() {
      setLoading(true);
      try {
        const [empArr, admArr] = await Promise.all([
          fetchEmploymentData(year),
          fetchAdmissionData(year),
        ]);
        if (!mounted) return;

        const deptList = empArr.map(d =>
                d.isDaytime === '야간' ? `${d.department} (야간)` : d.department
              );
              setDepartments([...new Set(deptList)]);

              // 선택 학과 없으면 첫 번째로 초기화
              const deptName = selectedDept || deptList[0];
              if (!selectedDept) setSelectedDept(deptName);

              // 선택된 학과명과 isDaytime 판별
              const isNight = deptName.includes('(야간)');
              const pureDept = deptName.replace(' (야간)', '');

              const emp = empArr.find(
                d => d.department === pureDept && (isNight ? d.isDaytime === '야간' : d.isDaytime === '주간')
              ) || empArr[0];

              const adm = admArr.find(
                d => d.department === pureDept && (isNight ? d.isDaytime === '야간' : d.isDaytime === '주간')
              ) || admArr[0];

        const employmentBreakdown = {
          labels: ['건강보험 직장가입자','해외 취업자','농림어업 종사자','개인창작활동 종사자','1인 창업자','프리랜서'],
          data: [
            emp.insuredEmployeesTotal,
            emp.overseasEmployeesTotal,
            emp.agricultureFisheryTotal,
            emp.individualCreatorsTotal,
            emp.selfEmployedTotal,
            emp.freelancersTotal,
          ]
        };

        setEmploymentData({
          ...emp,
          employmentBreakdown,
          topEmployers: [
            { name: "삼성전자", count: 5 },
            { name: "LG전자", count: 4 },
            { name: "네이버", count: 3 },
            { name: "카카오", count: 2 },
            { name: "현대자동차", count: 2 },
          ]
        });

        setAdmissionData({
          ...adm,
          topSchools: [
            { name: "서울대학교 대학원", count: 2 },
            { name: "KAIST", count: 1 },
            { name: "연세대학교 대학원", count: 1 },
            { name: "고려대학교 대학원", count: 1 },
            { name: "성균관대학교 대학원", count: 1 },
          ]
        });

      } catch (error) {
        console.error(error);
        if (mounted) {
          setEmploymentData(null);
          setAdmissionData(null);
        }
      } finally {
        if (mounted) setLoading(false);
      }
    }
    loadData();
    return () => { mounted = false; };
  }, [year, selectedDept]);

  const combinedData = useMemo(() => {
    if (!employmentData || !admissionData) return null;
    // 진학 세부
    const studyBreakdown = [
      admissionData.domesticKorJrCollege,
      admissionData.domesticCollege,
      admissionData.domesticGrad,
      admissionData.overseasKorJrCollege,
      admissionData.overseasCollege,
      admissionData.overseasGrad,
    ];
    const studyLabels = ['전문대학-국내','대학-국내','대학원-국내','전문대학-국외','대학-국외','대학원-국외'];

    const empLabels = employmentData.employmentBreakdown.labels;
    const empData = employmentData.employmentBreakdown.data;

    return {
      department: employmentData.department,
      total: employmentData.totalGraduates,
      employed: employmentData.totalEmployed,
      further: employmentData.totalAdmission,
      etc: employmentData.totalEtc,
      employmentRate: employmentData.employmentRate,
      admissionRate: employmentData.admissionRate,
      etcRate: employmentData.etcRate,
      empBar: { labels: empLabels, data: empData },
      studyBar: { labels: studyLabels, data: studyBreakdown },
      topEmployers: employmentData.topEmployers,
      topSchools: admissionData.topSchools
    };
  }, [employmentData, admissionData]);

  const donut = useMemo(() => {
    if (!combinedData) return { labels: [], datasets: [] };
    return {
      labels: ['취업','진학','기타'],
      datasets: [{
        data: [combinedData.employed, combinedData.further, combinedData.etc],
        backgroundColor: [BRAND, ALT, '#E5E7EB'],
        hoverOffset: 6,
        borderWidth: 2,
      }]
    };
  }, [combinedData]);

  const empBar = useMemo(() => combinedData ? {
    labels: combinedData.empBar.labels,
    datasets: [{ label:'명', data: combinedData.empBar.data, backgroundColor: BRAND_SOFT, borderColor: BRAND, borderWidth: 2, borderRadius: 10 }]
  } : null, [combinedData]);

  const studyBar = useMemo(() => combinedData ? {
    labels: combinedData.studyBar.labels,
    datasets: [{ label:'명', data: combinedData.studyBar.data, backgroundColor: ALT_SOFT, borderColor: ALT, borderWidth: 2, borderRadius: 10 }]
  } : null, [combinedData]);

  const barOpts = useMemo(()=>({
    indexAxis:'y',
    responsive:true,
    maintainAspectRatio:false,
    plugins:{ legend:{ display:false }, tooltip:{ callbacks:{ label:(ctx)=> `${ctx.raw}명` } } },
    scales:{ x:{ beginAtZero:true, ticks:{ precision:0 } }, y:{ grid:{ display:false } } }
  }),[]);

  return (
    <div className="w-full h-full mx-auto p-6 bg-white rounded-2xl shadow">
      <div className="flex flex-wrap items-center justify-between gap-3 mb-5">
        <h2 className="text-xl font-semibold">졸업생 진학·취업 현황</h2>
        <div className="flex items-center gap-2">
          <select value={selectedDept} onChange={e => setSelectedDept(e.target.value)}
            className="border border-[#028EA7] font-semibold rounded-lg p-2 bg-white focus:outline-none focus:ring-2 focus:ring-[#028EA7]">
            {departments.map(d => (
                <option key={d} value={d}>{d}</option>
            ))}
          </select>
          <div className="inline-flex gap-2 ml-2">
            {YEARS.map(y=>(
              <button key={y} onClick={()=>setYear(y)}
                className={`px-3 py-1.5 rounded-full border text-sm transition-all ${
                  year===y ? 'bg-[#028EA7] text-white border-[#028EA7] shadow' : 'border-gray-300 text-[#028EA7] hover:bg-[#e6f6f9]'
                }`}>{y}</button>
            ))}
          </div>
        </div>
      </div>

      {loading || !combinedData ? <p>데이터 불러오는 중...</p> : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-4 gap-3 mb-5">
            <div className="rounded-xl shadow border border-gray-200 p-4">
              <div className="text-sm font-semibold text-gray-500">총 졸업생 ({year})</div>
              <div className="text-2xl font-semibold">{combinedData.total} 명</div>
            </div>
            <div className="rounded-xl shadow border border-gray-200 p-4">
              <div className="text-sm font-semibold text-gray-500">취업 ({year})</div>
              <div className="text-2xl font-semibold">{combinedData.employed} 명</div>
              <div className="text-xs text-[#028EA7]">{combinedData.employmentRate}%</div>
            </div>
            <div className="rounded-xl shadow border border-gray-200 p-4">
              <div className="text-sm font-semibold text-gray-500">진학 ({year})</div>
              <div className="text-2xl font-semibold">{combinedData.further} 명</div>
              <div className="text-xs text-[#e2d92eff]">{combinedData.admissionRate}%</div>
            </div>
            <div className="rounded-xl shadow border border-gray-200 p-4">
              <div className="text-sm font-semibold text-gray-500">기타 ({year})</div>
              <div className="text-2xl font-semibold">{combinedData.etc} 명</div>
              <div className="text-xs text-gray-500">{combinedData.etcRate}%</div>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-4">
            <div className="rounded-2xl shadow border border-gray-200 p-4">
              <h3 className="text-base font-semibold mb-3">진로 구성 비율 ({year})</h3>
              <div className="h-[260px]">
                <Doughnut data={donut} options={{ responsive:true, maintainAspectRatio:false, plugins:{ legend:{ position:'bottom' }, tooltip:{ callbacks:{ label:(ctx)=> `${ctx.label}: ${ctx.raw}명 (${((ctx.raw/combinedData.total)*100).toFixed(1)}%)` } } }, cutout:'60%' }} />
              </div>
            </div>

            <div className="rounded-2xl shadow border border-gray-200 p-4">
              <div className="flex items-center justify-between mb-3">
                <h3 className="text-base font-semibold">취업 세부 분야 ({year})</h3>
                <span className="text-xs text-gray-500">표시단위: 명</span>
              </div>
              <div className="h-[260px]">
                <Bar data={empBar} options={barOpts} />
              </div>
            </div>

            <div className="rounded-2xl shadow border border-gray-200 p-4">
              <div className="flex items-center justify-between mb-3">
                <h3 className="text-base font-semibold">진학 세부 ({year})</h3>
                <span className="text-xs text-gray-500">표시단위: 명</span>
              </div>
              <div className="h-[260px]">
                <Bar data={studyBar} options={barOpts} />
              </div>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 mt-4">
            <div className="rounded-2xl shadow border border-gray-200 p-4">
              <h3 className="text-base font-semibold mb-2">주요 취업처 TOP5 ({year})</h3>
              <ul className="text-sm">
                {combinedData.topEmployers.map((e, idx)=>(
                  <li key={idx} className="flex items-center justify-between py-1 border-b border-gray-200 last:border-b-0">
                    <span className="truncate">{idx+1}. {e.name}</span>
                    <span className="text-gray-600">{e.count}명</span>
                  </li>
                ))}
              </ul>
            </div>
            <div className="rounded-2xl shadow border border-gray-200 p-4">
              <h3 className="text-base font-semibold mb-2">주요 진학처 TOP5 ({year})</h3>
              <ul className="text-sm">
                {combinedData.topSchools.map((u, idx)=>(
                  <li key={idx} className="flex items-center justify-between py-1 border-b border-gray-200 last:border-b-0">
                    <span className="truncate">{idx+1}. {u.name}</span>
                    <span className="text-gray-600">{u.count}명</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
