import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const Login = () => {
  const [showFindSection, setShowFindSection] = useState(false);
  const [findMode, setFindMode] = useState(null);
  const [idInput, setIdInput] = useState("");
  const [passwordInput, setPasswordInput] = useState("");
  const [emailInput, setEmailInput] = useState("");
  const [message, setMessage] = useState("");
  const [role, setRole] = useState("student"); // 역할 상태 추가

  const navigate = useNavigate();

  const handleFindClick = (mode) => {
    setFindMode(mode);
    setShowFindSection(true);
    setMessage("");
    setIdInput("");
    setEmailInput("");
  };

  const handleSubmit = () => {
    if (!idInput || !passwordInput) {
      setMessage("아이디와 비밀번호를 입력하세요.");
      return;
    }

    console.log("로그인 시도:", { idInput, passwordInput, role });

    // 로그인 성공 처리 이후 역할에 따라 페이지 이동
    if (role === "student") {
      navigate("/student");
    } else if (role === "staff") {
      navigate("/staff");
    } else if (role === "admin") {
      navigate("/admin");
    }
  };

  return (
    <div className="relative w-screen h-screen overflow-hidden">
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{ backgroundImage: "url('/yiu_background.jpeg')" }}
      ></div>
      <div className="absolute inset-0 bg-black bg-opacity-60"></div>

      <div className="relative z-10 flex items-center justify-center h-full">
        <div className="w-full max-w-md p-8 bg-white bg-opacity-90 rounded-xl shadow-2xl">
          <img
            src="/mascot.png"
            alt="용인대 마스코트"
            className="w-24 h-24 mx-auto mb-4"
          />
          <h1 className="text-2xl font-bold text-center text-gray-800 mb-6">
            용인대학교 로그인
          </h1>

          <div className="mb-4">
            <label className="block mb-1 text-gray-700">아이디</label>
            <input
              type="text"
              value={idInput}
              onChange={(e) => setIdInput(e.target.value)}
              placeholder="아이디를 입력하세요"
              className="w-full px-4 py-2 border rounded-md"
            />
          </div>

          <div className="mb-4">
            <label className="block mb-1 text-gray-700">비밀번호</label>
            <input
              type="password"
              value={passwordInput}
              onChange={(e) => setPasswordInput(e.target.value)}
              placeholder="비밀번호를 입력하세요"
              className="w-full px-4 py-2 border rounded-md"
            />
          </div>

          {/* 역할 선택 */}
          <div className="mb-6">
            <label className="block mb-1 text-gray-700">역할 선택</label>
            <select
              value={role}
              onChange={(e) => setRole(e.target.value)}
              className="w-full px-4 py-2 border rounded-md"
            >
              <option value="student">학생</option>
              <option value="staff">교원</option>
              <option value="admin">임직원</option>
            </select>
          </div>

          {message && (
            <p className="mb-3 text-sm text-red-600 font-semibold">{message}</p>
          )}

          <button
            onClick={handleSubmit}
            className="w-full py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition mb-4"
          >
            로그인
          </button>

          {!showFindSection && (
            <div className="flex justify-between mt-4 text-sm text-blue-600">
              <button onClick={() => handleFindClick("id")} className="hover:underline">
                아이디 찾기
              </button>
              <button onClick={() => handleFindClick("password")} className="hover:underline">
                비밀번호 찾기
              </button>
            </div>
          )}

          {showFindSection && (
            <div className="mt-6 bg-gray-100 p-4 rounded-md">
              <h2 className="text-lg font-semibold mb-3 text-gray-700">
                {findMode === "password" ? "비밀번호 재설정" : "아이디 찾기"}
              </h2>
              <label className="block mb-1 text-gray-700">
                등록된 이메일을 입력하세요
              </label>
              <input
                type="email"
                value={emailInput}
                onChange={(e) => setEmailInput(e.target.value)}
                placeholder="이메일을 입력하세요"
                className="w-full px-3 py-2 mb-3 border rounded-md"
              />
              <p className="mb-3 text-sm text-red-600 font-semibold">{message}</p>
              <div className="flex justify-between">
                <button
                  onClick={handleSubmit}
                  className="px-4 py-2 bg-blue-600 text-white rounded-md"
                >
                  제출
                </button>
                <button
                  onClick={() => setShowFindSection(false)}
                  className="px-4 py-2 bg-gray-400 text-white rounded-md"
                >
                  취소
                </button>
              </div>
            </div>
          )}

          <div className="mt-6 text-center">
            <span className="text-gray-600">아직 회원이 아니신가요? </span>
            <Link to="/signup" className="text-blue-600 hover:underline">
              회원가입
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;