import React from 'react';
import { X } from 'lucide-react';
// ⭐️ 9번 컴포넌트를 내용물로 사용하기 위해 임포트합니다. ⭐️
import StudentCountBox from './09StudentCountBox'; 
// ⭐️ 12번 컴포넌트 (졸업생 진학/취업)를 임포트합니다. ⭐️
import GraduateOutcomes from './12GraduateCareerChart';

/**
 * BasicStatModal: 비회원 페이지의 KPI 카드 클릭 시 상세 통계를 보여주는 모달 틀
 * @param {boolean} show - 모달 표시 여부
 * @param {function} onClose - 모달 닫기 핸들러
 * @param {string} statType - 보여줄 통계 유형 ('students', 'faculty' 등)
 */
export default function BasicStatModal({ show, onClose, statType }) {
  if (!show) return null;

  // statType에 따라 렌더링할 내용을 분기합니다.
  const ModalContent = () => {
    switch (statType) {
      case 'students':
        // 재학생 수 상세 분석
        return <StudentCountBox initialDepartment="식품조리학부" />;
        
      case 'recruitment':
        // ⭐️ 졸업생 취업률 상세 분석 (12번 컴포넌트 사용) ⭐️
        return <GraduateOutcomes initialDepartment="AI학부" initialYear="2024" />;

      default:
        return (
            <div className="p-6 text-center text-gray-600">
                <p className="text-xl font-semibold mb-2">통계 정보 없음</p>
                <p>선택된 통계 유형({statType})에 대한 상세 분석 컴포넌트가 정의되지 않았습니다.</p>
            </div>
        );
    }
  };
  
  // ⭐️ recruitment 타입일 때만 너비를 확장하는 조건부 클래스를 사용합니다. ⭐️
  const isWide = statType === 'recruitment';
  const maxWidthClass = isWide ? 'max-w-5xl' : 'max-w-3xl';
  
  // 만약 다른 컴포넌트도 크기가 다르다면 아래 max-w-3xl만 변경하면 됩니다.
  // const maxWidthClass = 'max-w-3xl';

  return (
    // 배경 오버레이: 고정(fixed) 위치에 배치하고 스크롤 가능하도록 설정
    <div className="fixed inset-0 bg-white/50 backdrop-blur-sm z-50 flex items-center justify-center overflow-y-auto">
      
      {/* ⭐️ 모달 박스: recruitment일 때 max-w-5xl로 확장 ⭐️ */}
      <div className={`bg-white w-full ${maxWidthClass} rounded-2xl shadow-xl relative my-8`}>
        
        {/* 닫기 버튼 */}
        <button 
          onClick={onClose} 
          className="absolute top-4 right-4 text-gray-600 hover:text-black z-10 p-2"
          aria-label="모달 닫기"
        >
          <X size={24} />
        </button>

        {/* 모달 콘텐츠: 9번 컴포넌트가 여기에 렌더링됩니다. */}
        <div className="p-6">
          {ModalContent()}
        </div>
      </div>
    </div>
  );
}
