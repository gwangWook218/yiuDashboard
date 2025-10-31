// src/pages/Signup.jsx
import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { primeCsrf, checkIdAvailability, signupRequest } from "../lib/auth";

/* ---------------------------------------------
   메인 컴포넌트
--------------------------------------------- */
export default function Signup() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    role: "student",
    id: "",
    email: "",
    pw: "",
    confirmPw: "",
  });
  const [idChecked, setIdChecked] = useState(false);
  const [loadingCheck, setLoadingCheck] = useState(false);
  const [loadingSubmit, setLoadingSubmit] = useState(false);
  const submittingRef = useRef(false);

  // 최초 진입 시 CSRF 쿠키 프라임
  useEffect(() => { primeCsrf(); }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "role") {
      setForm((prev) => ({ ...prev, role: value, id: "" }));
      setIdChecked(false);
    } else if (name === "id") {
      setForm((prev) => ({ ...prev, id: value }));
      setIdChecked(false);
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleIdCheck = async () => {
    if (!form.id) return alert("아이디를 입력하세요");
    try {
      setLoadingCheck(true);
      const result = await checkIdAvailability(form.id);
      if (result.exists) {
        alert("이미 사용 중인 아이디입니다.");
        setIdChecked(false);
      } else {
        alert("사용 가능한 아이디입니다.");
        setIdChecked(true);
      }
    } catch (err) {
      console.error("check-id failed:", err);
      alert("중복확인에 실패했습니다. (권한/형식 문제 가능)");
      setIdChecked(false);
    } finally {
      setLoadingCheck(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (submittingRef.current) return;

    if (!idChecked) return alert("아이디 중복 확인을 해주세요");
    if (!form.email) return alert("이메일을 입력하세요");
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      return alert("올바른 이메일 형식을 입력하세요");
    if (form.pw !== form.confirmPw)
      return alert("비밀번호가 일치하지 않습니다");

    try {
      setLoadingSubmit(true);
      submittingRef.current = true;

      await signupRequest({
        loginId: form.id,
        email: form.email,
        password: form.pw,
        passwordCheck: form.confirmPw,
        role: form.role,
      });

      alert(`${form.role === "student" ? "학생" : "교직원"} 회원가입이 완료되었습니다.`);
      if (form.role === "student") navigate("/");
      else navigate("/");
    } catch (err) {
      console.error("signup failed:", err);
      alert(err.message || "회원가입 실패");
    } finally {
      setLoadingSubmit(false);
      submittingRef.current = false;
    }
  };

  const idLabel = form.role === "student" ? "학번(아이디)" : "사번(아이디)";
  const idPlaceholder = form.role === "student" ? "학번(아이디) 입력" : "사번(아이디) 입력";
  const idInputProps =
    form.role === "student"
      ? { inputMode: "numeric", pattern: "[0-9]+", title: "숫자만 입력하세요" }
      : {};

  return (
    <div
      className="relative flex justify-center items-center h-screen bg-cover bg-center"
      style={{ backgroundImage: "url(/yiu_background.jpeg)" }}
    >
      <div className="absolute inset-0 bg-black/40"></div>

      <div className="relative w-full max-w-md bg-white rounded-2xl shadow-2xl p-10">
        <div className="flex justify-center mb-6">
          <img src="/mascot.png" alt="Mascot" className="h-24 w-24" />
        </div>

        <h1 className="text-2xl font-semibold text-center mb-8 text-[#2F5664]">
          회원가입
        </h1>

        <form onSubmit={handleSubmit} className="space-y-5">
          {/* 역할 선택 */}
          <div>
            <label className="block text-sm font-medium mb-1">역할 선택</label>
            <select
              name="role"
              value={form.role}
              onChange={handleChange}
              className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
            >
              <option value="student">학생</option>
              <option value="staff">교직원</option>
            </select>
          </div>

          {/* 아이디 */}
          <div>
            <label className="block text-sm font-medium mb-1">{idLabel}</label>
            <div className="flex gap-2">
              <input
                type="text"
                name="id"
                value={form.id}
                onChange={handleChange}
                className="flex-1 border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                placeholder={idPlaceholder}
                required
                autoComplete="username"
                {...idInputProps}
              />
              <button
                type="button"
                onClick={handleIdCheck}
                className="px-4 rounded-lg text-white transition"
                style={{ backgroundColor: "#3B7F91" }}
                disabled={loadingCheck}
                title={loadingCheck ? "확인 중..." : "중복 확인"}
              >
                {loadingCheck ? "확인 중…" : "중복 확인"}
              </button>
            </div>
          </div>

          {/* 이메일 */}
          <div>
            <label className="block text-sm font-medium mb-1">이메일</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
              placeholder="비밀번호/아이디 찾기에 사용할 이메일"
              required
              autoComplete="email"
            />
          </div>

          {/* 비밀번호 */}
          <div>
            <label className="block text-sm font-medium mb-1">비밀번호</label>
            <input
              type="password"
              name="pw"
              value={form.pw}
              onChange={handleChange}
              className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
              placeholder="비밀번호 입력"
              required
              autoComplete="new-password"
              minLength={8}
            />
          </div>

          {/* 비밀번호 확인 */}
          <div>
            <label className="block text-sm font-medium mb-1">비밀번호 확인</label>
            <input
              type="password"
              name="confirmPw"
              value={form.confirmPw}
              onChange={handleChange}
              className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
              placeholder="비밀번호 재입력"
              required
              autoComplete="new-password"
              minLength={8}
            />
          </div>

          {/* 회원가입 버튼 */}
          <button
            type="submit"
            className="w-full text-white py-3 text-base font-medium rounded-lg transition"
            style={{ backgroundColor: loadingSubmit ? "#94a3b8" : "#2F5664" }}
            disabled={loadingSubmit}
          >
            {loadingSubmit ? "가입 처리 중…" : "회원가입"}
          </button>

          {/* 로그인으로 이동 */}
          <p
            className="text-center text-sm text-[#2F5664] mt-4 cursor-pointer hover:underline"
            onClick={() => navigate("/")}
          >
            로그인으로 돌아가기
          </p>
        </form>
      </div>
    </div>
  );
}
