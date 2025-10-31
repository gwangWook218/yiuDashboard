import React, { useMemo, useState } from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

const BRAND = '#028EA7';
const BRAND_SOFT = 'rgba(2, 142, 167, 0.6)';
const ALT = '#EFE63B';
const ALT_SOFT = 'rgba(221, 230, 123, 0.35)';

const LABELS = [
  '100~95', '95~90', '90~85', '85~80',
  '80~75', '75~70', '70~65', '65~60', '60 미만'
];
const GPA_VALUES = [4.5, 4.0, 3.5, 3.0, 2.5, 2.0, 1.5, 1.0, 0];

const DEPARTMENTS = [
  'AI학부','격기지도학과','격기학과','경영정보학과','경영학과','경영학부','경찰행정학과','경호학과','골프학과','관광경영학과',
  '관광학과','국악과','국제학부','군사학과','기타모집단위','노인복지학과','노인재활복지학과','동양무예학과','디지털미디어학과','라이프디자인학과',
  '무도스포츠산업학과','무도스포츠학과','무용과','무용학과','문화관광학과','문화재보존학과','문화재학과','문화콘텐츠학과','물류통계정보학과','물리치료학과',
  '뮤지컬·실용음악과','뮤지컬・연극학과','미디어디자인학과','미디어예술학부','미용경영학과','뷰티케어학과','뷰티헬스케어학과','사회복지학과','사회체육학과','산업디자인학과',
  '산업환경보건학과','생명과학과','스포츠레저학과','스포츠미디어학과','식품영양학과','식품조리학부','실용음악과','연극학과','영어과','영화영상학과',
  '유도경기지도학과','유도학과','자연과학부','중국학과','체육학과','체육학부','컴퓨터과학과','컴퓨터정보학과','컴퓨터정보학부','태권도경기지도학과',
  '태권도학과','특수체육교육과','특수체육학과','환경ㆍ생명학부','환경학과','회화학과'
];

const YEARS = ['2023','2024'];

const seeded = (s) => {
  let h = 2166136261 >>> 0;
  for (let i = 0; i < s.length; i++) { h ^= s.charCodeAt(i); h = Math.imul(h, 16777619); }
  h ^= h >>> 13; h = Math.imul(h, 0x85ebca6b); h ^= h >>> 16; h = Math.imul(h, 0xc2b2ae35);
  return (h >>> 0) / 4294967295;
};

function makeGradeDistribution(seedKey, total, isSemester1) {
  const base = isSemester1
    ? [1.6, 1.1, 1.4, 0.9, 0.5, 0.1, 0.05, 0.05, 0.3]
    : [1.4, 1.2, 1.5, 1.0, 0.6, 0.1, 0.05, 0.05, 0.2];

  const weights = base.map((w, i) => w * (0.9 + seeded(`${seedKey}-${i}`) * 0.22));
  const sumW = weights.reduce((a, b) => a + b, 0);
  const raw = weights.map((w) => (w / sumW) * total);
  const ints = raw.map(Math.floor);

  let diff = total - ints.reduce((a, b) => a + b, 0);
  let idx = 0; while (diff > 0) { ints[idx % ints.length]++; diff--; idx++; }
  return ints;
}

function estimateGPA(dist) {
  const mids = [97.5, 92.5, 87.5, 82.5, 77.5, 72.5, 67.5, 62.5, 55];
  const totalStudents = dist.reduce((a, b) => a + b, 0) || 1;
  const weightedScoreSum = dist.reduce((acc, count, i) => acc + count * mids[i], 0);
  const meanScore = weightedScoreSum / totalStudents;

  return +((meanScore / 100) * 4.5).toFixed(2);
}

const gpaToScore = (gpa) => +((gpa / 4.5) * 100).toFixed(2);

function makeDatum({ department, year, semester }) {
  const isSemester1 = semester === '1';
  const baseTotal = 40 + Math.floor(seeded(`${department}-${year}-${semester}-maj`) * 35);
  const total = Math.max(20, baseTotal);

  const dist = makeGradeDistribution(`${department}-${year}-${semester}`, total, isSemester1);
  const GPA = estimateGPA(dist);
  const score = gpaToScore(GPA);

  return { score, GPA, totalStudents: total, distribution: dist };
}

export default function MajorSemesterCompare(){
  const [department, setDepartment] = useState('경영정보학과');
  const [year, setYear] = useState('2024');
  const [mode, setMode] = useState('percent');

  const s1 = useMemo(()=> makeDatum({ department, year, semester:'1' }), [department, year]);
  const s2 = useMemo(()=> makeDatum({ department, year, semester:'2' }), [department, year]);

  const toPercent = (arr)=>{
    const sum = arr.reduce((a,b)=>a+b,0)||1;
    return arr.map(v=> +((v/sum)*100).toFixed(1));
  };

  const s1Data = mode==='count'? s1.distribution : toPercent(s1.distribution);
  const s2Data = mode==='count'? s2.distribution : toPercent(s2.distribution);

  const chartData = useMemo(()=> ({
    labels: LABELS,
    datasets: [
      {
        label: '1학기',
        data: s1Data,
        backgroundColor: BRAND_SOFT,
        borderColor: BRAND,
        borderWidth: 2,
        borderRadius: 8,
      },
      {
        label: '2학기',
        data: s2Data,
        backgroundColor: ALT_SOFT,
        borderColor: ALT,
        borderWidth: 2,
        borderRadius: 8,
      },
    ]
  }), [s1Data, s2Data]);

  const options = useMemo(()=> ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      title: { display: false },
      legend: { display: true, position: 'top', labels: { usePointStyle: true } },
      tooltip: {
        callbacks: {
          label: (ctx) => {
            const val = ctx.raw;
            return mode==='count' ? `${ctx.dataset.label}: ${val}명` : `${ctx.dataset.label}: ${val}%`;
          }
        }
      }
    },
    scales: {
      x: {
        title: { display: true },
        ticks: { display: true },
        grid: { display:false },
        stacked: false,
      },
      y: {
        beginAtZero: true,
        title: { display: true, text: mode==='count' ? '학생수(명)' : '비율(%)' },
        ticks: { precision: 0 }
      }
    }
  }), [mode]);

return (
  <div className="w-full h-full max-w-5xl mx-auto p-6 bg-white rounded-2xl shadow-xl font-['Inter']">
    <div className="flex flex-wrap items-center justify-between gap-3 mb-5 pb-4">
      <h2 className="text-xl font-semibold text-gray-800">
        전공 성적 1학기 vs 2학기 비교
      </h2>
      <div className="flex items-center gap-3">
        <select
          value={department}
          onChange={(e) => setDepartment(e.target.value)}
          className="border border-[#028EA7] font-semibold rounded-lg p-2 bg-white focus:outline-none focus:ring-2 focus:ring-[#028EA7] shadow-sm"
        >
          {DEPARTMENTS.map((dep)=>(<option key={dep}>{dep}</option>))}
        </select>

        <div className="inline-flex gap-1">
          {YEARS.map((y)=> (
            <button key={y} onClick={()=>setYear(y)}
              className={`px-3 py-1.5 rounded-full border text-sm transition-all ${
                year===y
                  ? 'bg-[#028EA7] text-white border-[#028EA7] shadow'
                  : 'border-gray-300 text-[#028EA7] hover:bg-[#e6f6f9]'
              }`}
            >{y}</button>
          ))}
        </div>

        <div className="inline-flex rounded-xl border border-gray-300 p-1 bg-white ml-2">
          {['count','percent'].map((m)=> (
            <button key={m} onClick={()=>setMode(m)}
              className={`px-3 py-1.5 rounded-lg text-sm transition-all ${
                mode===m
                  ? 'bg-[#028EA7] text-white shadow'
                  : 'text-[#028EA7] hover:bg-[#e6f6f9]'
              }`}
            >{m==='count'?'명':'%'}
            </button>
          ))}
        </div>
      </div>
    </div>

    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
      <div className="rounded-xl shadow border border-gray-200 p-4 bg-white">
        <div className="text-xs text-gray-500 mb-1">1학기 전공 성적 </div>
        <div className="mt-1 flex items-end gap-4">
          <div className="text-xl font-semibold">{s1.score}<span className="text-sm text-gray-500"> / 100</span></div>
          <div className="text-xl font-semibold">{s1.GPA}<span className="text-sm text-gray-500"> / 4.5</span></div>
          <div className="text-xl font-semibold">{s1.totalStudents}<span className="text-sm text-gray-500"> 명</span></div>
        </div>
      </div>
      <div className="rounded-xl shadow border border-gray-200 p-4 bg-white">
        <div className="text-xs text-gray-500 mb-1">2학기 전공 성적 </div>
        <div className="mt-1 flex items-end gap-4">
          <div className="text-xl font-semibold">{s2.score}<span className="text-sm text-gray-500"> / 100</span></div>
          <div className="text-xl font-semibold">{s2.GPA}<span className="text-sm text-gray-500"> / 4.5</span></div>
          <div className="text-xl font-semibold">{s2.totalStudents}<span className="text-sm text-gray-500"> 명</span></div>
        </div>
      </div>
    </div>

    <div className="h-[360px] rounded-2xl shadow border border-gray-200 p-4">
      <Bar data={chartData} options={options} />
    </div>
  </div>
);
}