import React from "react";
import { useNavigate } from "react-router-dom";

const WelcomePage = () => {
  const navigate = useNavigate();

  return (
    <div className="relative w-screen h-screen overflow-hidden font-sans">
      {/* 배경 이미지 */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{ backgroundImage: "url('/yiu_background.jpeg')" }}
      ></div>

      {/* 어두운 오버레이 */}
      <div className="absolute inset-0 bg-black bg-opacity-60"></div>

      {/* 콘텐츠 박스 */}
      <div className="relative z-10 flex items-center justify-center h-full text-white px-4">
        <div className="w-full max-w-md bg-white bg-opacity-90 rounded-xl shadow-2xl p-8 text-center">
          {/* 마스코트 */}
          <img
            src="/mascot.png"
            alt="용인대 마스코트"
            className="w-24 h-24 mx-auto mb-4 drop-shadow-lg"
          />

          {/* 제목 */}
          <h1 className="text-3xl font-bold mb-2 text-gray-800">
            환영합니다!
          </h1>

          {/* 설명 */}
          <p className="text-base mb-6 text-gray-700">
            아래에서 로그인 또는 비회원을 선택하세요.
          </p>

          {/* 버튼 영역 */}
          <div className="flex flex-col gap-4">
            <button
              onClick={() => navigate("/login")}
              className="w-full py-3 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg shadow-md transition"
            >
              회원 (로그인)
            </button>
            <button
              onClick={() => navigate("/guest")}
              className="w-full py-3 bg-gray-400 hover:bg-gray-500 text-white font-semibold rounded-lg shadow-md transition"
            >
              비회원
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default WelcomePage;