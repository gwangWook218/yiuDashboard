import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function FindPassword() {
  const navigate = useNavigate();
  const [role, setRole] = useState("student"); // 'student' | 'staff'
  const [notice, setNotice] = useState("");

  // 학생 폼
  const [stu, setStu] = useState({
    studentId: "",
    name: "",
    email: "",
    code: "",
    newPw: "",
    confirmPw: "",
  });
  const [stuCodeSent, setStuCodeSent] = useState(false);

  // 교직원 폼
  const [staff, setStaff] = useState({
    staffId: "",
    name: "",
    email: "",
    code: "",
    newPw: "",
    confirmPw: "",
  });
  const [staffCodeSent, setStaffCodeSent] = useState(false);

  const handleStuChange = (e) => setStu({ ...stu, [e.target.name]: e.target.value });
  const handleStaffChange = (e) => setStaff({ ...staff, [e.target.name]: e.target.value });

  // 인증코드 발송 (데모)
  const sendCode = async () => {
    setNotice("");
    if (role === "student") {
      if (!stu.studentId || !stu.email) {
        setNotice("학번과 이메일을 입력해 주세요.");
        return;
      }
      // TODO: /api/auth/password/code-send (student)
      setStuCodeSent(true);
      setNotice("인증코드를 이메일로 발송했어요. 메일함을 확인해 주세요.");
    } else {
      if (!staff.staffId || !staff.email) {
        setNotice("아이디/사번과 이메일을 입력해 주세요.");
        return;
      }
      // TODO: /api/auth/password/code-send (staff)
      setStaffCodeSent(true);
      setNotice("인증코드를 이메일로 발송했어요. 메일함을 확인해 주세요.");
    }
  };

  // 비밀번호 재설정 (데모)
  const resetPassword = async (e) => {
    e.preventDefault();
    setNotice("");

    if (role === "student") {
      if (!stuCodeSent) {
        setNotice("먼저 인증코드를 발송해 주세요.");
        return;
      }
      if (!stu.code) {
        setNotice("인증코드를 입력해 주세요.");
        return;
      }
      if (stu.newPw !== stu.confirmPw) {
        setNotice("새 비밀번호가 서로 일치하지 않습니다.");
        return;
      }
      // TODO: /api/auth/password/reset (student)
      alert("비밀번호가 재설정되었습니다. 로그인해 주세요.");
      navigate("/login");
    } else {
      if (!staffCodeSent) {
        setNotice("먼저 인증코드를 발송해 주세요.");
        return;
      }
      if (!staff.code) {
        setNotice("인증코드를 입력해 주세요.");
        return;
      }
      if (staff.newPw !== staff.confirmPw) {
        setNotice("새 비밀번호가 서로 일치하지 않습니다.");
        return;
      }
      // TODO: /api/auth/password/reset (staff)
      alert("비밀번호가 재설정되었습니다. 로그인해 주세요.");
      navigate("/login");
    }
  };

  return (
    <div
      className="relative flex justify-center items-center min-h-screen bg-cover bg-center"
      style={{ backgroundImage: "url(/yiu_background.jpeg)" }}
    >
      {/* 배경 톤 다운 */}
      <div className="absolute inset-0 bg-black/40" />

      <div className="relative w-full max-w-md bg-white rounded-2xl shadow-2xl p-8">
        {/* 로고 */}
        <div className="flex justify-center mb-6">
          <img src="/mascot.png" alt="Mascot" className="h-20 w-20" />
        </div>

        <h1 className="text-2xl font-semibold text-center mb-6 text-[#2F5664]">
          비밀번호 찾기
        </h1>

        {/* 역할 선택 탭 */}
        <div className="grid grid-cols-2 gap-1 p-1 bg-gray-100 rounded-lg mb-4">
          <button
            type="button"
            onClick={() => { setRole("student"); setNotice(""); }}
            className={`py-2 rounded-md text-sm font-medium transition ${
              role === "student" ? "bg-white shadow" : "bg-transparent hover:bg-white/70"
            }`}
          >
            학생
          </button>
          <button
            type="button"
            onClick={() => { setRole("staff"); setNotice(""); }}
            className={`py-2 rounded-md text-sm font-medium transition ${
              role === "staff" ? "bg-white shadow" : "bg-transparent hover:bg-white/70"
            }`}
          >
            교직원
          </button>
        </div>

        {/* 안내/알림 */}
        {notice && (
          <div className="mb-4 rounded-lg border border-amber-300 bg-amber-50 text-amber-800 px-3 py-2 text-sm">
            {notice}
          </div>
        )}

        {/* 학생 폼 (학번 = 아이디) */}
        {role === "student" && (
          <form onSubmit={resetPassword} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1">학번(아이디)</label>
              <input
                type="text"
                name="studentId"
                value={stu.studentId}
                onChange={handleStuChange}
                className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                placeholder="학번 입력"
                required
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium mb-1">이름</label>
                <input
                  type="text"
                  name="name"
                  value={stu.name}
                  onChange={handleStuChange}
                  className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                  placeholder="이름 입력"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">이메일</label>
                <input
                  type="email"
                  name="email"
                  value={stu.email}
                  onChange={handleStuChange}
                  className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                  placeholder="가입 시 등록한 이메일"
                  required
                />
              </div>
            </div>

            {/* 인증코드 발송 */}
            <button
              type="button"
              onClick={sendCode}
              className="w-full bg-gray-600 text-white py-2 rounded-lg hover:bg-gray-700 transition"
            >
              인증코드 보내기
            </button>

            {/* 코드 입력 + 새 비밀번호 (코드 발송 후 노출) */}
            {stuCodeSent && (
              <>
                <div>
                  <label className="block text-sm font-medium mb-1">인증코드</label>
                  <input
                    type="text"
                    name="code"
                    value={stu.code}
                    onChange={handleStuChange}
                    className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                    placeholder="메일로 받은 인증코드"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">새 비밀번호</label>
                  <input
                    type="password"
                    name="newPw"
                    value={stu.newPw}
                    onChange={handleStuChange}
                    className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                    placeholder="새 비밀번호 입력"
                    minLength={8}
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">새 비밀번호 확인</label>
                  <input
                    type="password"
                    name="confirmPw"
                    value={stu.confirmPw}
                    onChange={handleStuChange}
                    className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                    placeholder="새 비밀번호 재입력"
                    minLength={8}
                    required
                  />
                </div>

                <button
                  type="submit"
                  className="w-full bg-[#2F5664] text-white py-3 rounded-lg font-medium hover:bg-[#3B7F91] transition"
                >
                  비밀번호 재설정
                </button>
              </>
            )}

            <p
              className="text-center text-sm text-[#2F5664] mt-4 cursor-pointer hover:underline"
              onClick={() => navigate("/")}
            >
              로그인으로 돌아가기
            </p>
          </form>
        )}

        {/* 교직원 폼 */}
        {role === "staff" && (
          <form onSubmit={resetPassword} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1">아이디 / 사번</label>
              <input
                type="text"
                name="staffId"
                value={staff.staffId}
                onChange={handleStaffChange}
                className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                placeholder="아이디(또는 사번) 입력"
                required
              />
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="block text-sm font-medium mb-1">이름</label>
                <input
                  type="text"
                  name="name"
                  value={staff.name}
                  onChange={handleStaffChange}
                  className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                  placeholder="이름 입력"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium mb-1">이메일</label>
                <input
                  type="email"
                  name="email"
                  value={staff.email}
                  onChange={handleStaffChange}
                  className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                  placeholder="가입 시 등록한 이메일"
                  required
                />
              </div>
            </div>

            {/* 인증코드 발송 */}
            <button
              type="button"
              onClick={sendCode}
              className="w-full bg-gray-600 text-white py-2 rounded-lg hover:bg-gray-700 transition"
            >
              인증코드 보내기
            </button>

            {/* 코드 입력 + 새 비밀번호 */}
            {staffCodeSent && (
              <>
                <div>
                  <label className="block text-sm font-medium mb-1">인증코드</label>
                  <input
                    type="text"
                    name="code"
                    value={staff.code}
                    onChange={handleStaffChange}
                    className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                    placeholder="메일로 받은 인증코드"
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">새 비밀번호</label>
                  <input
                    type="password"
                    name="newPw"
                    value={staff.newPw}
                    onChange={handleStaffChange}
                    className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                    placeholder="새 비밀번호 입력"
                    minLength={8}
                    required
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium mb-1">새 비밀번호 확인</label>
                  <input
                    type="password"
                    name="confirmPw"
                    value={staff.confirmPw}
                    onChange={handleStaffChange}
                    className="w-full border rounded-lg px-3 py-2 focus:outline-none focus:ring-2 focus:ring-[#2F5664]"
                    placeholder="새 비밀번호 재입력"
                    minLength={8}
                    required
                  />
                </div>

                <button
                  type="submit"
                  className="w-full bg-[#2F5664] text-white py-3 rounded-lg font-medium hover:bg-[#3B7F91] transition"
                >
                  비밀번호 재설정
                </button>
              </>
            )}

            <p
              className="text-center text-sm text-[#2F5664] mt-4 cursor-pointer hover:underline"
              onClick={() => navigate("/")}
            >
              로그인으로 돌아가기
            </p>
          </form>
        )}
      </div>
    </div>
  );
}
