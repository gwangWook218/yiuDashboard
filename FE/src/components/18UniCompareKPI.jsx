import React, { useMemo } from "react";
import { Radar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  Tooltip,
  Legend,
  Title,
  RadialLinearScale,
  Filler,
  PointElement,
  LineElement,
  CategoryScale,
  LinearScale,
  BarElement,
} from "chart.js";

// ChartJS에 필요한 모든 요소 등록
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  PointElement,
  LineElement,
  Tooltip,
  Legend,
  Title,
  RadialLinearScale,
  Filler
);

// MARK: - Constants and Data Definition

// UI 색상 상수 (UniCompareKPI.jsx의 BRAND 사용)
const BRAND = "#028EA7";
const UNI_THEME_COLORS = {
  "용인대": "#028EA7", // Teal/Navy (청록)
  "명지대": "#EF4444", // Red (빨강)
  "강남대": "#4B5563", // Gray (회색)
};

const YEARS = ["2022", "2023", "2024"]; // 모든 년도 고정 사용
const UNIVERSITIES = ["용인대", "명지대", "강남대"];

const DATA = {
  fillRate: [
    { uni: "용인대", values: { 2022: 102.1, 2023: 106.2, 2024: 105.4 } },
    { uni: "명지대", values: { 2022: 127.4, 2023: 129.7, 2024: 127.0 } },
    { uni: "강남대", values: { 2022: 106.1, 2023: 109.0, 2024: 104.8 } },
  ],
  competition: [
    { uni: "용인대", values: { 2022: 9.7, 2023: 7.4, 2024: 8.0 } },
    { uni: "명지대", values: { 2022: 10.4, 2023: 9.7, 2024: 12.0 } },
    { uni: "강남대", values: { 2022: 7.4, 2023: 7.9, 2024: 6.8 } },
  ],
  enrollRate: [
    { uni: "용인대", values: { 2022: 98.3, 2023: 98.1, 2024: 98.0 } },
    { uni: "명지대", values: { 2022: 99.2, 2023: 98.3, 2024: 98.4 } },
    { uni: "강남대", values: { 2022: 97.5, 2023: 97.6, 2024: 97.5 } },
  ],
  securedRate: [
    { uni: "용인대", values: { 2022: 65.5, 2023: 66.1, 2024: 67.3 } },
    { uni: "명지대", values: { 2022: 62.5, 2023: 63.49, 2024: 62.19 } },
    { uni: "강남대", values: { 2022: 75.32, 2023: 73.99, 2024: 67.1 } },
  ],
  employmentRate: [
    { uni: "용인대", values: { 2022: 68.2, 2023: 67.5, 2024: 69.7 } },
    { uni: "명지대", values: { 2022: 67.2, 2023: 65.6, 2024: 64.2 } },
    { uni: "강남대", values: { 2022: 63.8, 2023: 62.2, 2024: 60.8 } },
  ],
};

// 년도별 색상 정의 (차트 폴리곤 색상)
const COLOR_2022 = "#3B82F6"; // Blue-500
const COLOR_SOFT_2022 = "rgba(59, 130, 246, 0.3)";
const COLOR_2023 = "#F59E0B"; // Amber-500
const COLOR_SOFT_2023 = "rgba(245, 158, 11, 0.3)";
const COLOR_2024 = "#10B981"; // Emerald-500
const COLOR_SOFT_2024 = "rgba(16, 185, 129, 0.3)";

const YEAR_COLORS = [
  { year: "2022", border: COLOR_2022, background: COLOR_SOFT_2022 },
  { year: "2023", border: COLOR_2023, background: COLOR_SOFT_2023 },
  { year: "2024", border: COLOR_2024, background: COLOR_SOFT_2024 },
];

const GRID_COLOR = "rgba(156,163,175,.35)";

// 데이터 정규화를 위한 KPI별 최소/최대 범위 및 단위 설정 (시각적 차이 강조)
const KPI_SCALES_CONFIG = {
    fillRate: { min: 90, max: 135, unit: '%' },
    competition: { min: 0, max: 13, unit: ':1' },
    enrollRate: { min: 95, max: 100, unit: '%' },
    securedRate: { min: 60, max: 80, unit: '%' },
    employmentRate: { min: 60, max: 70, unit: '%' },
};

// 차트 라벨
const KPI_LABELS_CLEAN = {
  fillRate: "충원율",
  competition: "경쟁률",
  enrollRate: "등록률",
  securedRate: "전임교원 확보율",
  employmentRate: "취업률",
};
const KPI_KEYS = Object.keys(DATA);

/**
 * 정규화 헬퍼 함수: KPI 값을 0-100% 스케일로 변환
 */
const normalize = (value, kpiKey) => {
  const scale = KPI_SCALES_CONFIG[kpiKey];
  if (!scale) return 0;
  
  const range = scale.max - scale.min;
  if (range <= 0) return 50; 
  
  const normalized = 100 * (value - scale.min) / range;
  return Math.max(0, Math.min(100, normalized));
};

/**
 * 특정 대학의 년도별 데이터를 추출하고 정규화하여 Radar Chart 데이터 객체를 반환합니다.
 */
const getUniChartData = (uniName) => {
  const labels = Object.values(KPI_LABELS_CLEAN);

  // 모든 년도를 데이터셋으로 사용
  const datasets = YEARS.map((year) => { 
    const rawDataPoints = []; 
    
    const normalizedDataPoints = KPI_KEYS.map(kpiKey => {
      const kpiData = DATA[kpiKey].find(item => item.uni === uniName);
      const rawValue = kpiData ? kpiData.values[year] : 0;
      rawDataPoints.push(rawValue); 
      
      return normalize(rawValue, kpiKey); 
    });

    const colorScheme = YEAR_COLORS.find(c => c.year === year) || YEAR_COLORS[0];

    return {
      label: `${year}년`,
      data: normalizedDataPoints, 
      rawData: rawDataPoints, // 툴팁을 위해 원본 데이터 저장
      
      fill: true,
      backgroundColor: colorScheme.background,
      borderColor: colorScheme.border,
      pointBackgroundColor: colorScheme.border,
      pointBorderColor: "#fff",
      pointHoverBackgroundColor: "#fff",
      pointHoverBorderColor: colorScheme.border,
      borderWidth: 2,
    };
  });

  return {
    labels: labels,
    datasets: datasets,
  };
};

// MARK: - Main Component

export default function UniCompareRadarChart() {
  // 년도 선택 상태를 제거하고 모든 년도를 사용하도록 고정
  const selectedYears = YEARS; 

  // 컴포넌트 전체에서 사용할 차트 데이터들 (모든 년도 포함)
  const allUniChartData = useMemo(() => {
    return UNIVERSITIES.map(uni => ({
      uniName: uni,
      data: getUniChartData(uni), // 년도 인자 제거
    }));
  }, []); 

  // 차트 옵션 (모든 차트에 공통 적용)
  const chartOptions = useMemo(() => ({
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top', 
        labels: { boxWidth: 12, padding: 20 }
      },
      tooltip: {
        callbacks: {
          label: function(context) {
            let label = context.dataset.label || '';
            const dataIndex = context.dataIndex;
            
            const rawData = context.dataset.rawData; 
            const rawValue = rawData ? rawData[dataIndex] : 'N/A';
            
            const kpiKey = KPI_KEYS[dataIndex];
            const scale = KPI_SCALES_CONFIG[kpiKey];
            const unit = scale ? scale.unit : '';
            
            if (label) {
                label += ': ';
            }
            label += rawValue.toFixed(1) + unit;
            
            return label;
          }
        }
      }
    },
    scales: {
      r: {
        angleLines: { display: true, color: GRID_COLOR },
        grid: { color: GRID_COLOR },
        suggestedMin: 0,
        max: 100, // 정규화된 최대값
        
        pointLabels: {
          display: true,
          font: { size: 12, weight: '600' },
          color: '#4B5563', 
        },
        ticks: {
            display: true, 
            stepSize: 25, 
            color: '#A1A1AA', 
            backdropColor: 'rgba(255, 255, 255, 0.75)', 
            backdropPadding: 2,
            callback: function(value) {
              return value + '%'; // 정규화된 값에 % 표시
            }
        }
      }
    },
    elements: {
        line: { tension: 0.1, borderWidth: 3 },
        point: { radius: 4, hoverRadius: 6 }
    }
  }), []);


  return (
    // UniCompareKPI의 전체 컨테이너 디자인 적용 (배경, 그림자, 중앙 정렬)
    <div className="w-full h-full mx-auto p-6 bg-gray-50 rounded-2xl shadow-lg font-sans">
      <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between border-b pb-4 mb-6">
        <h2 className="text-2xl font-extrabold tracking-tight text-slate-900">
          종합 비교 리포트
        </h2>
        <div className="text-sm text-slate-500 mt-2 sm:mt-0">학교별 KPI 년도 추이 (2022년~2024년)</div>
      </div>
      
      {/* 비교 년도 선택 버튼 그룹 제거됨 */}
      
      <p className="text-sm text-gray-600 mb-8 p-3 bg-indigo-50 rounded-lg border border-indigo-200">
        *각 KPI는 관찰 범위 기준으로 **0%~100%로 정규화**되어 수치 차이가 명확히 부각됩니다. 마우스를 데이터 포인트 위에 올리면 (툴팁) **원본 수치**를 확인할 수 있습니다.
      </p>

      {/* 3개의 개별 Radar 차트 컨테이너 */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-5">
        {allUniChartData.map((item) => {
          const uniColor = UNI_THEME_COLORS[item.uniName];

          return (
            <div 
              key={item.uniName} 
              // Card height increase: h-[380px] -> h-[500px]
              className="bg-white p-4 rounded-xl shadow-lg border-t-4 h-[500px]" 
              style={{ borderColor: uniColor }} 
            >
              <h4 
                className="text-xl font-bold mb-4 text-center" 
                style={{ color: uniColor }}
              >
                {item.uniName} 
              </h4>
              {/* Chart canvas height increase: h-[280px] -> h-[400px] */}
              <div className="h-[400px] w-full"> 
                {/* 년도 선택 제거로 인해 항상 모든 데이터셋이 표시됨 */}
                <Radar data={item.data} options={chartOptions} />
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
