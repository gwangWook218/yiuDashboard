import { useState } from 'react';
import { Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

const graduateStudyData = {
  safety: {
    name: '보건환경안전학과',
    pathways: [
      '보건직/환경직 대학원',
      '산업보건/산업위생 전공 대학원',
      '환경과학 대학원',
      '공공보건대학원',
      '위생관리 대학원'
    ],
    chart: {
      labels: ['보건대학원', '환경대학원', '기타'],
      values: [40, 35, 25]
    }
  },
  bio: {
    name: '바이오생명공학과',
    pathways: [
      '생명공학 대학원',
      '의학/약학 대학원',
      '바이오헬스 전공',
      '국공립연구소 진학',
      '기초과학 연구원 과정'
    ],
    chart: {
      labels: ['생명공학', '보건의료계열', '기타'],
      values: [45, 30, 25]
    }
  },
  food: {
    name: '식품조리학부',
    pathways: [
      '식품영양학 대학원',
      '조리과학 석사과정',
      '위생학 전공 과정',
      '외식경영학 대학원',
      '푸드스타일링 과정'
    ],
    chart: {
      labels: ['식품영양', '조리/위생', '기타'],
      values: [50, 30, 20]
    }
  },
  ai: {
    name: 'AI융합학부',
    pathways: [
      'AI/데이터사이언스 대학원',
      'IT정책대학원',
      '산업공학 전공',
      '융합소프트웨어 과정',
      '빅데이터 분석 석사'
    ],
    chart: {
      labels: ['AI/데이터', '공학/정책', '기타'],
      values: [55, 30, 15]
    }
  }
};

export default function GraduateStudyStatus() {
  const [selected, setSelected] = useState('bio');
  const major = graduateStudyData[selected];

  const chartData = {
    labels: major.chart.labels,
    datasets: [
      {
        label: '진학 비율',
        data: major.chart.values,
        backgroundColor: ['#3B7F91', '#72BCD4', '#E5E7EB'],
        borderWidth: 1
      }
    ]
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top'
      }
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-300 w-full h-full min-h-[465px] mt-6">
      {/* 헤더 */}
      <div className="px-6 py-4 border-b border-gray-200">
        <h1 className="text-xl font-bold text-center">〈 졸업생 진학 현황 〉</h1>
      </div>

      {/* 전공 선택 버튼 */}
      <div className="flex justify-center gap-3 py-6 flex-wrap border-b border-gray-200">
        {Object.entries(graduateStudyData).map(([id, { name }]) => (
          <button
            key={id}
            onClick={() => setSelected(id)}
            className={`px-4 py-2 rounded-full border font-medium text-sm ${
              selected === id ? 'bg-[#3B7F91] text-white' : 'bg-white text-gray-700'
            }`}
          >
            {name}
          </button>
        ))}
      </div>

      {/* 본문 */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 items-center p-6">
        {/* 진학 분야 목록 */}
        <div className="rounded-xl border border-gray-300 shadow-sm p-4 bg-white h-[270px] overflow-y-auto">
          <h2 className="text-lg text-center font-semibold text-gray-800 mb-4">-주요 진학 분야-</h2>
          <ul className="list-disc list-inside text-gray-700 space-y-2">
            {major.pathways.map((item, idx) => (
              <li key={idx}>{item}</li>
            ))}
          </ul>
        </div>

        {/* 도넛 차트 */}
        <div className="rounded-xl border border-gray-300 shadow-sm p-6 bg-white h-[270px] flex flex-col items-center justify-center">
          <h2 className="text-lg text-center font-semibold text-gray-800 mb-2">-진학률-</h2>
          <Doughnut data={chartData} options={options} />
        </div>
      </div>
    </div>
  );
}
