import React from "react";

const Signup = () => {
  return (
    <div className="relative w-screen h-screen overflow-hidden">
      {/* 배경 이미지 */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{ backgroundImage: "url('/yiu_background.jpeg')" }}
      ></div>

      {/* 반투명 검정 오버레이 */}
      <div className="absolute inset-0 bg-black bg-opacity-60"></div>

      {/* 회원가입 박스 */}
      <div className="relative z-10 flex items-center justify-center h-full">
        <div className="w-full max-w-md p-8 bg-white bg-opacity-90 rounded-xl shadow-2xl">
          {/* 마스코트 이미지 */}
          <img
            src="/mascot.png"
            alt="용인대 마스코트"
            className="w-24 h-24 mx-auto mb-4"
          />

          <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">
            용인대학교 회원가입
          </h1>

          <form>
            {/* 아이디 입력 */}
            <div className="mb-4">
              <label className="block mb-1 text-gray-700">아이디</label>
              <input
                type="text"
                placeholder="아이디를 입력하세요"
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
              {/* 중복확인 버튼 */}
              <button
                type="button"
                className="mt-2 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
              >
                중복 확인
              </button>
            </div>

            {/* 이메일 입력 추가 */}
            <div className="mb-4">
              <label className="block mb-1 text-gray-700">이메일</label>
              <input
                type="email"
                placeholder="이메일을 입력하세요"
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* 비밀번호 입력 */}
            <div className="mb-4">
              <label className="block mb-1 text-gray-700">비밀번호</label>
              <input
                type="password"
                placeholder="비밀번호를 입력하세요"
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* 비밀번호 확인 */}
            <div className="mb-6">
              <label className="block mb-1 text-gray-700">비밀번호 확인</label>
              <input
                type="password"
                placeholder="비밀번호를 다시 입력하세요"
                className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>

            {/* 회원가입 버튼 */}
            <button
              type="submit"
              className="w-full py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition"
            >
              회원가입
            </button>
          </form>

          {/* 하단 링크 */}
          <div className="mt-6 text-center text-sm text-gray-600">
            이미 회원이신가요?{" "}
            <a href="/" className="text-blue-600 hover:underline">
              로그인
            </a>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Signup;