import React from "react";

export default function ParkingGuide() {
  return (
    <div className="grid grid-cols-1 md:grid-cols-3 text-sm divide-y divide-gray-200 md:divide-y-0 md:divide-x">
      <div className="p-4 md:p-5">
        <p className="font-semibold text-gray-800 mb-2">정기권(학기권) 신청자</p>
        <ul className="list-disc pl-5 text-gray-700 space-y-1">
          <li>1학기 20,000원</li>
        </ul>
      </div>

      <div className="p-4 md:p-5">
        <p className="font-semibold text-gray-800 mb-2">정기권 이용자 외 교내 출입 차량</p>
        <ul className="list-disc pl-5 text-gray-700 space-y-1">
          <li>정문 무인정산기 카드 결제</li>
          <li>(삼성페이, 카카오T 모바일앱 사용 가능)</li>
        </ul>
      </div>

      <div className="p-4 md:p-5">
        <p className="font-semibold text-gray-800 mb-2">요금</p>
        <ul className="list-disc pl-5 text-gray-700 space-y-1">
          <li>기본: 30분간 무료</li>
          <li>추가: 15분당 1,000원</li>
          <li>일일주차료 상한: 20,000원</li>
        </ul>
      </div>
    </div>
  );
}
