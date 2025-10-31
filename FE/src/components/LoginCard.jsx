// src/components/LoginCard.jsx
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FiEye, FiEyeOff } from "react-icons/fi";
import { loginRequest, persistSession, clearSession } from "../lib/auth";

export default function LoginCard() {
  const navigate = useNavigate();
  const [role, setRole] = useState("student");
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [showPw, setShowPw] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const onSubmit = async (e) => {
    e.preventDefault();
    setError("");
    if (!userId || !password) return setError("아이디와 비밀번호를 입력하세요.");

    try {
      setLoading(true);
      const data = await loginRequest({ id: userId, pw: password });
      const actualRole = data.user.role.toUpperCase().replace(/^ROLE_/, "");
      const normalizedSelectedRole = role === "student" ? "STUDENT" : "PROFESSOR";

      if (normalizedSelectedRole !== actualRole) {
        setError("선택한 권한과 계정 권한이 일치하지 않습니다.");
        clearSession(); // 잘못된 세션 제거
        return;
      }

      // JWT 토큰 저장
      persistSession(data, true);

//       alert("로그인 성공!");
      navigate(normalizedSelectedRole === "STUDENT" ? "/student" : "/report", { replace: true });
    } catch (err) {
      setError(err.message || "로그인 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="w-full max-w-sm bg-white/95 text-gray-900 rounded-xl shadow-lg p-5 backdrop-blur">
      <div className="grid grid-cols-2 gap-1 p-1 bg-gray-100 rounded-lg mb-4">
        <button
          type="button"
          onClick={() => setRole("student")}
          className={`py-2 rounded-md text-sm font-medium transition ${
            role === "student" ? "bg-white shadow" : "bg-transparent hover:bg-white/70"
          }`}
        >
          학생
        </button>
        <button
          type="button"
          onClick={() => setRole("staff")}
          className={`py-2 rounded-md text-sm font-medium transition ${
            role === "staff" ? "bg-white shadow" : "bg-transparent hover:bg-white/70"
          }`}
        >
          교직원
        </button>
      </div>

      <form onSubmit={onSubmit} className="space-y-3">
        {error && <p className="text-xs text-red-600">{error}</p>}

        <div>
          <label className="block text-sm mb-1">
            {role === "student" ? "학번 / 아이디" : "사번 / 아이디"}
          </label>
          <input
            type="text"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
            placeholder={role === "student" ? "학번 또는 아이디 입력" : "사번 또는 아이디 입력"}
            className="w-full rounded-lg border px-3 py-2"
            required
            autoComplete="username"
          />
        </div>

        <div>
          <label className="block text-sm mb-1">비밀번호</label>
          <div className="relative">
            <input
              type={showPw ? "text" : "password"}
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="비밀번호 입력"
              className="w-full rounded-lg border px-3 py-2 pr-10"
              required
              autoComplete="current-password"
            />
            <button
              type="button"
              onClick={() => setShowPw((v) => !v)}
              className="absolute inset-y-0 right-2 flex items-center px-2 text-gray-500 hover:text-gray-700"
              aria-label={showPw ? "비밀번호 숨기기" : "비밀번호 표시"}
              title={showPw ? "비밀번호 숨기기" : "비밀번호 표시"}
            >
              {showPw ? <FiEyeOff size={18} /> : <FiEye size={18} />}
            </button>
          </div>
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full mt-2 bg-[#2F5664] text-white py-2 rounded-lg font-medium hover:opacity-90 disabled:opacity-60"
        >
          {loading ? "로그인 중..." : "로그인"}
        </button>

        <div className="flex justify-between text-xs mt-2">
          <Link to="/findid" className="text-[#2F5664] hover:underline">
            아이디 찾기
          </Link>
          <Link to="/signup" className="text-[#2F5664] hover:underline">
            회원가입
          </Link>
        </div>
      </form>
    </div>
  );
}
