import React, { useEffect, useRef, useState } from "react";

export default function CreditProgressDetailCard({ userId: userIdProp }) {
  const [creditData, setCreditData] = useState([
    { label: "전체", earned: 0, required: 130, color: "bg-[#98D2E5]" },
    { label: "전공", earned: 0, required: 54, color: "bg-[#72BCD4]" },
    { label: "기초전공", earned: 0, required: 12, color: "bg-[#028EA7]" },
    { label: "교양필수", earned: 0, required: 13, color: "bg-[#006C7F]" },
    { label: "교양선택", earned: 0, required: 16, color: "bg-[#02556E]" },
  ]);

  const [creditJson, setCreditJson] = useState(null);
  const [transferCredit, setTransferCredit] = useState(0);
  const [thesisStatus, setThesisStatus] = useState("제출 필요");
  const [toeicScore, setToeicScore] = useState(null);
  const [certificationStatus, setCertificationStatus] = useState("미취득");

  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState("");

  const inputRef = useRef(null);

  const API_BASE =
    (import.meta.env?.VITE_API_URL ? String(import.meta.env.VITE_API_URL) : "")
      .replace(/\/$/, "");
  const apiPath = (p) => (API_BASE ? `${API_BASE}${p}` : p);
  const LOAD_URL = (loginId) => apiPath(`/api/grades/credit-progress?loginId=${loginId}`);
  const UPLOAD_URL = (save) => apiPath(`/api/grades/credit-progress/upload?save=${save ? "true" : "false"}`);

  const authHeaders = (extra = {}) => {
    const token = localStorage.getItem("accessToken");
    return token ? { Authorization: `Bearer ${token}`, ...extra } : { ...extra };
  };

  const userId = userIdProp ?? localStorage.getItem("userId");

  /* ───────── 초기 로드 ───────── */
  useEffect(() => {
    if (!userId) return;
    let alive = true;

    (async () => {
      try {
        setLoading(true);

        const res = await fetch(LOAD_URL(userId), { headers: authHeaders() });
        if (!res.ok) throw new Error(`로드 실패 (HTTP ${res.status})`);
        const json = await res.json();

        const resInfo = await fetch(apiPath(`/api/grades/my-info-add`), { headers: authHeaders() });
        if (!resInfo.ok) throw new Error(`로드 실패 (HTTP ${resInfo.status})`);
        const infoJson = await resInfo.json();

        if (alive && Array.isArray(json) && json.length > 0) {
          setCreditData(json.filter(item => item.category !== undefined).map(item => ({
            label: item.category,
            earned: item.earned ?? 0,
            required: item.required ?? 0,
            color: creditData.find(c => c.label === item.category)?.color ?? "bg-gray-300"
          })));
          const first = json[0] ?? {};
          setTransferCredit(first.transferCredit ?? transferCredit);
          setThesisStatus(first.thesisStatus ?? thesisStatus);
          setToeicScore(first.toeicScore ?? toeicScore);
          setCertificationStatus(first.certificationStatus ?? certificationStatus);
        }
        setThesisStatus(infoJson.thesisStatus ?? thesisStatus);
        setTransferCredit(infoJson.transferCredits ?? transferCredit);
        setToeicScore(infoJson.toeicScore ?? toeicScore);
        setCertificationStatus(infoJson.certificateStatus ?? certificationStatus);
      } catch (e) {
        if (alive) setErr(e?.message || "데이터를 불러오지 못했습니다.");
      } finally {
        if (alive) setLoading(false);
      }
    })();

    return () => { alive = false; };
  }, [userId]);

  /* ───────── 파일 업로드 ───────── */
  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;
    if (!(file.type === "application/pdf" || file.name.toLowerCase().endsWith(".pdf"))) {
      setErr("PDF 파일만 업로드 가능합니다.");
      setSelectedFile(null);
      return;
    }

    setSelectedFile(file);
    setErr("");
    setLoading(true);

    try {
      const formData = new FormData();
      formData.append("file", file);
      formData.append("userId", String(userId));

      const res = await fetch(UPLOAD_URL(true), {
        method: "POST",
        headers: authHeaders(),
        body: formData,
      });

      if (!res.ok) {
        // 저장 실패 시 파싱만 확인
        const probe = await fetch(UPLOAD_URL(false), {
          method: "POST",
          headers: authHeaders(),
          body: formData,
        });
        if (!probe.ok) throw new Error(`업로드 실패 (HTTP ${probe.status})`);
      }

      // 재조회
      const reload = await fetch(LOAD_URL(userId), { headers: authHeaders() });
      if (!reload.ok) throw new Error(`조회 실패 (HTTP ${reload.status})`);
      const json = await reload.json();

      setCreditData(json.map(item => ({
        label: item.category,
        earned: item.earned ?? 0,
        required: item.required ?? 0,
        color: creditData.find(c => c.label === item.category)?.color ?? "bg-gray-300"
      })));
      setTransferCredit(json[0]?.transferCredit ?? 0);
      setThesisStatus(json[0]?.thesisStatus ?? "제출 필요");
      setToeicScore(json[0]?.toeicScore ?? null);
      setCertificationStatus(json[0]?.certificationStatus ?? "미취득");

      setSelectedFile(null);
      if (inputRef.current) inputRef.current.value = "";
    } catch (err2) {
      console.error(err2);
      setErr(err2?.message || "업로드/조회 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  /* ───────── 부수적인 학업정보 입력 ───────── */
  const handleSaveMyInfo = async () => {
    setLoading(true);
    setErr("");

    try {
      const payload = {
        transferCredits: transferCredit,
        thesisStatus: thesisStatus,
        toeicScore: toeicScore,
        certificateStatus: certificationStatus,
      };

      const res = await fetch(apiPath(`/api/grades/student-info/save`), {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...authHeaders(),
        },
        body: JSON.stringify(payload),
      });

      if (!res.ok) throw new Error(`저장 실패 (HTTP ${res.status})`);
      const json = await res.json();

      // 성공 시 상태 반영
      setTransferCredit(json.transferCredits ?? transferCredit);
      setThesisStatus(json.thesisStatus ?? thesisStatus);
      setToeicScore(json.toeicScore ?? toeicScore);
      setCertificationStatus(json.certificateStatus ?? certificationStatus);

      alert("저장이 완료되었습니다.");
    } catch (e) {
      console.error(e);
      setErr(e?.message || "저장 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow p-8 w-full h-full">
      <h2 className="text-xl font-bold mb-5 text-center">〈 이수 학점 상세 분석 〉</h2>
      <hr className="my-4 border-t border-gray-300" />

      {/* Main Credit Progress Section */}
      <div className="space-y-6">
        {creditData.map((item, index) => {
          const percentage = (item.earned / item.required) * 100;
          const displayPercentage = isNaN(percentage) ? 0 : Math.min(percentage, 100);

          return (
            <div key={index}>
              <div className="flex justify-between items-end mb-2">
                <span className="text-base font-medium text-gray-700">
                  {item.label} 이수 학점
                </span>
                <div className="flex space-x-3">
                  <input
                    type="number"
                    value={item.earned}
                    onChange={(e) => handleCreditChange(index, 'earned', e.target.value)}
                    className="w-20 text-center p-2 rounded border border-gray-300 text-sm"
                  />
                  <span>/</span>
                  <input
                    type="number"
                    value={item.required}
                    onChange={(e) => handleCreditChange(index, 'required', e.target.value)}
                    className="w-20 text-center p-2 rounded border border-gray-300 text-sm"
                  />
                  <span>학점</span>
                </div>
              </div>
              <div className="w-full bg-gray-200 rounded-full h-5">
                <div
                  className={`h-5 rounded-full ${item.color}`}
                  style={{ width: `${displayPercentage}%` }}
                ></div>
              </div>
              <div className="text-right text-xs text-gray-500 mt-2">
                {displayPercentage.toFixed(1)}%
              </div>
            </div>
          );
        })}
      </div>

      <hr className="my-6 border-t border-gray-300" />

      {/* PDF Upload Section */}
      <div className="flex flex-col items-center mb-8">
        <label className="text-lg font-medium text-gray-700 mb-4">이수구분별 성적표 업로드 (PDF)</label>
        <input
          ref={inputRef}
          type="file"
          accept=".pdf"
          onChange={handleFileUpload}
          disabled={loading}
          className="w-full text-base text-gray-500
          file:mr-4 file:py-2 file:px-4
          file:rounded-full file:border-0
          file:text-base file:font-semibold
          file:bg-cyan-50 file:text-cyan-700
          hover:file:bg-cyan-100 disabled:opacity-60"
        />
        {selectedFile && (
          <p className="text-sm text-gray-500 mt-4">
            선택된 파일: {selectedFile.name}
          </p>
        )}
        {loading && <p className="text-sm text-cyan-700 mt-2">처리 중…</p>}
        {err && <p className="text-sm text-red-500 mt-2">{err}</p>}
      </div>

      <hr className="my-6 border-t border-gray-300" />

      {/* Transfer Credit, Thesis, and Other Specs Section */}
      <div className="mt-8 text-base text-gray-600 leading-8 space-y-4">
        <div className="flex items-center space-x-4">
          <p className="flex-shrink-0">편입 인정 학점:</p>
          <input
            type="number"
            value={transferCredit}
            onChange={(e) => setTransferCredit(parseInt(e.target.value, 10) || 0)}
            className="w-20 text-center p-2 rounded border border-gray-300 text-base"
          />
          <p>학점</p>
        </div>
        <div className="flex items-center space-x-4">
          <p className="flex-shrink-0">졸업 논문 제출 여부:</p>
          <select
            value={thesisStatus}
            onChange={(e) => setThesisStatus(e.target.value)}
            className="p-2 rounded border border-gray-300 text-base"
          >
            <option value="제출필요">제출필요</option>
            <option value="제출함">제출함</option>
          </select>
        </div>
        <div className="flex items-center space-x-4">
          <p className="flex-shrink-0">TOEIC 성적:</p>
          <input
            type="number"
            value={toeicScore || ''}
            onChange={(e) => setToeicScore(e.target.value ? parseInt(e.target.value, 10) : null)}
            className="w-24 text-center p-2 rounded border border-gray-300 text-base"
            placeholder="점수"
          />
          <p>점</p>
        </div>
        <div className="flex items-center space-x-4">
          <p className="flex-shrink-0">자격증 취득 여부:</p>
          <select
            value={certificationStatus}
            onChange={(e) => setCertificationStatus(e.target.value)}
            className="p-2 rounded border border-gray-300 text-base"
          >
            <option value="미취득">미취득</option>
            <option value="취득">취득</option>
          </select>
        </div>
        <div className="mt-4">
          <button
            onClick={handleSaveMyInfo}
            disabled={loading}
            className="px-4 py-2 bg-cyan-600 text-white rounded hover:bg-cyan-700 disabled:opacity-50"
          >
            저장
          </button>
          {err && <p className="text-sm text-red-500 mt-2">{err}</p>}
        </div>
      </div>
    </div>
  );
}
