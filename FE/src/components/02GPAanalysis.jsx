import React, { useEffect, useMemo, useRef, useState } from "react";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";

ChartJS.register(
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    BarElement,
    Title,
    Tooltip,
    Legend
);

/* ───────── 환경 설정 ─────────
    - 배포/개발 둘 다 안전하게: VITE_API_URL이 있으면 절대경로 사용, 없으면 /api 프록시 경로 사용 */
const API_BASE =
    (import.meta.env?.VITE_API_URL ? String(import.meta.env.VITE_API_URL) : "")
        .replace(/\/$/, "");

const apiPath = (p) => (API_BASE ? `${API_BASE}${p}` : p);

/* ───────── 학기 라벨 ───────── */
const semesters = [
    "1학년 1학기", "1학년 2학기",
    "2학년 1학기", "2학년 2학기",
    "3학년 1학기", "3학년 2학기",
    "4학년 1학기", "4학년 2학기",
];
const totalSemesters = semesters.length;

/* ───────── API 경로 ───────── */
const UPLOAD_URL = (save) => apiPath(`/api/grades/semester/upload?save=${save ? "true" : "false"}`);
const LOAD_URL = (loginId) => apiPath(`/api/grades/semester?loginId=${loginId}`);

/* ───────── 인증 헤더 ───────── */
function authHeaders(extra = {}) {
    const token = localStorage.getItem("accessToken");
    return token
        ? { Authorization: `Bearer ${token}`, ...extra }
        : { ...extra };
}

/* ───────── userId 확보 (DB의 PK만 사용) ───────── */
function getUserIdFallback(propUserId) {
    if (propUserId != null && !Number.isNaN(Number(propUserId))) {
        return Number(propUserId);
    }

    // JWT 안에서 후보 키들
    const token = localStorage.getItem("accessToken");
    if (token && token.split(".").length === 3) {
        try {
            const payload = JSON.parse(atob(token.split(".")[1]));
            const candidates = [
                payload.id, payload.userPk, payload.user_id, payload.uid, payload.pk,
                payload.user?.id, payload.account?.id,
            ].filter((v) => v != null);
            for (const c of candidates) {
                const n = Number(c);
                if (Number.isFinite(n)) return n;
            }
        } catch {}
    }

    // 로그인 시 세션에 저장된 값
    try {
        const raw = localStorage.getItem("authUser");
        if (raw) {
            const u = JSON.parse(raw);
            const n = Number(u?.id);
            if (Number.isFinite(n)) return n;
        }
    } catch {}

    // 최후: userId 키 (학번 형태는 제외)
    const fromLS = localStorage.getItem("userId");
    if (fromLS) {
        const n = Number(fromLS);
        const looksLikeStudentNo = /^\s*20\d{6}\s*$/.test(fromLS);
        if (Number.isFinite(n) && !looksLikeStudentNo) return n;
    }

    return null;
}

/* ───────── 서버 응답 → 학기 매핑 ───────── */
function mapServerRecordsToSemesters(records = []) {
    const filled = Array.from({ length: totalSemesters }, () => ({ gpa: null, credits: null }));

    const toIndex = (rec) => {
        const order = rec.semesterOrder ?? rec.order ?? rec.idx ?? rec.index ?? rec.semesterIndex ?? null;
        if (order != null) {
            const i = Number(order) - 1;
            if (i >= 0 && i < totalSemesters) return i;
        }
        const gradeYear = rec.gradeYear ?? rec.grade ?? rec.year ?? null;
        const halfRaw = rec.semester ?? rec.term ?? rec.half ?? null;
        const halfNum = Number(halfRaw);
        if (Number(gradeYear) >= 1 && Number(gradeYear) <= 4 && (halfNum === 1 || halfNum === 2)) {
            return (Number(gradeYear) - 1) * 2 + (halfNum - 1);
        }
        return null;
    };

    let cursor = 0;
    for (const r of records) {
        const i = toIndex(r);
        const g = r.gpa ?? r.avg ?? r.gradePoint ?? r.score ?? null;
        const cRaw = r.credits ?? r.credit ?? r.units ?? r.point ?? null;
        const c = cRaw == null ? null : Number(cRaw);

        if (i != null) {
            filled[i].gpa = g == null ? null : Number(g);
            filled[i].credits = c;
        } else {
            while (cursor < totalSemesters && filled[cursor].gpa != null) cursor++;
            if (cursor < totalSemesters) {
                filled[cursor].gpa = g == null ? null : Number(g);
                filled[cursor].credits = c;
                cursor++;
            }
        }
    }
    return filled;
}

/* ───────── 메인 컴포넌트 ───────── */
export default function PerformanceRoadmapGraph({ userId: userIdProp }) {
    const [semesterData, setSemesterData] = useState(
        semesters.map(() => ({ gpa: null, credits: null }))
    );
    const [selectedFile, setSelectedFile] = useState(null);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState("");
    const inputRef = useRef(null);

    const userId = getUserIdFallback(userIdProp);
    const loginId = userId;

    /* ───────── 초기 로드 ───────── */
    useEffect(() => {
        if (!userId) return;
        let alive = true;

        (async () => {
            try {
                setLoading(true);
                console.log("[LOAD] GET", LOAD_URL(loginId), "loginId:", loginId);
                const res = await fetch(LOAD_URL(loginId), { headers: authHeaders() });
                const body = await res.text().catch(() => "");
                console.log("[LOAD] status", res.status, "body:", body);
                if (!res.ok) throw new Error(`로드 실패 (HTTP ${res.status})`);
                let json;
                try { json = JSON.parse(body); } catch { json = []; }
                const mapped = mapServerRecordsToSemesters(json);
                if (alive) setSemesterData(mapped);
            } catch (e) {
                if (alive) setErr(e?.message || "저장된 성적을 불러오지 못했습니다.");
            } finally {
                if (alive) setLoading(false);
            }
        })();

        return () => { alive = false; };
    }, [userId]);

    /* ───────── 파일 선택 시: 업로드(+저장) 후 재로드 ───────── */
    const handleFileChange = async (e) => {
        const fileList = e.target.files;
        const file = fileList && fileList.length > 0 ? fileList[0] : null;

        console.debug("[upload] 선택된 파일:", file?.name, file?.type, file?.size);
        console.log("[upload] userId:", userId, "API_BASE:", API_BASE);
        if (!file) {
            setErr("선택된 파일이 없습니다. 다시 선택해주세요.");
            return;
        }
        if (!(file.type === "application/pdf" || file.name.toLowerCase().endsWith(".pdf"))) {
            setErr("PDF 파일만 업로드할 수 있습니다.");
            setSelectedFile(null);
            return;
        }
        if (!userId || !Number.isFinite(Number(userId))) {
            setErr("userId가 필요합니다. (로그인/토큰에서 DB PK를 확보하세요)");
            return;
        }
        if (file.size === 0) {
            setErr("빈 PDF 파일입니다. 다시 저장 후 업로드하세요.");
            return;
        }

        setSelectedFile(file);
        setErr("");

        const uploadOnce = async (saveFlag) => {
            const fd = new FormData();
            fd.append("file", file);
            fd.append("userId", String(userId));

            const url = UPLOAD_URL(saveFlag);
            console.log("[UPLOAD]", url, "file:", file.name, "size:", file.size);

            let res;
            try {
                res = await fetch(url, {
                    method: "POST",
                    headers: authHeaders(), // FormData → Content-Type 자동
                    body: fd,
                    // credentials: "omit"  // 쿠키 불필요(permitAll) → CORS 단순화
                });
            } catch (netErr) {
                console.error("[UPLOAD] 네트워크 오류:", netErr);
                throw new Error("업로드 요청 자체가 보내지지 않았습니다. (네트워크/프록시 점검)");
            }

            const text = await res.text().catch(() => "");
            console.log(`[UPLOAD] status=${res.status} len=${text.length}`, text.slice(0, 200));
            return { ok: res.ok, status: res.status, text };
        };

        try {
            setLoading(true);

            // 1차: 저장 모드
            let up = await uploadOnce(true);

            // 저장 실패면 파싱만 확인
            if (!up.ok) {
                console.warn("[upload] save=true 실패 → save=false 재시도");
                const probe = await uploadOnce(false);
                if (!probe.ok) {
                    throw new Error(probe.text || `업로드 실패 (HTTP ${probe.status})`);
                } else {
                    setErr("파싱은 성공했지만 저장은 실패했습니다. (userId/외래키 확인)");
                }
                up = probe; // 파싱 성공 응답을 사용하도록 업데이트
            }

            // 2차: 재조회
            console.log("[RELOAD] GET", LOAD_URL(userId));
            const reload = await fetch(LOAD_URL(userId), { headers: authHeaders() });
            const body = await reload.text().catch(() => "");
            console.log("[RELOAD] status", reload.status, "body:", body);

            let json = [];
            if (reload.ok) {
                try { json = JSON.parse(body); } catch {}
            }

            // ✅ 조회 결과가 비어 있으면 업로드 응답 데이터를 그래프에 바로 표시
            let mapped = [];
            if (Array.isArray(json) && json.length > 0) {
                mapped = mapServerRecordsToSemesters(json);
            } else {
                console.warn("[RELOAD] 조회 결과 비어있음 → 업로드 데이터 사용");
                // 업로드 응답 텍스트가 JSON일 경우만 사용
                try {
                    const uploadJson = JSON.parse(up.text || "[]");
                    mapped = mapServerRecordsToSemesters(uploadJson);
                } catch {
                    mapped = [];
                }
            }

            setSemesterData(mapped);

            if (inputRef.current) inputRef.current.value = "";
            setSelectedFile(null);


            if (inputRef.current) inputRef.current.value = "";
            setSelectedFile(null);
        } catch (err2) {
            console.error(err2);
            setErr(err2?.message || "업로드/저장 중 오류가 발생했습니다.");
        } finally {
            setLoading(false);
        }
    };

    /* ───────── 차트 계산 ───────── */
    const {
        chartData,
        lastFilledIndex,
        currentCumulativeGpaValue,
        maxAchievableGpaValue,
    } = useMemo(() => {
        let lastIdx = -1;
        for (let i = 0; i < semesters.length; i++) {
            if (semesterData[i].gpa !== null) lastIdx = Math.max(lastIdx, i);
        }

        let currentSum = 0, currentCreds = 0;
        const projected = Array(semesters.length).fill(null);

        // 1. 성적이 이미 입력된 학기까지의 실제 누적 평점 계산 및 차트 데이터 채우기
        for (let i = 0; i <= lastIdx; i++) {
            const { gpa, credits } = semesterData[i];
            if (gpa !== null && credits && credits > 0) {
                currentSum += gpa * credits;
                currentCreds += credits;
            }
            // 실제 성적 기반 누적 평점
            projected[i] = currentCreds > 0 ? currentSum / currentCreds : null;
        }

        // 2. 최대 달성 가능 평점 계산 (lastIdx 이후 학기는 4.5점 만점 가정)
        let maxAchievableSum = currentSum; 
        let maxAchievableCreds = currentCreds;
        
        // lastIdx + 1 부터 모든 학기에 대해 4.5 만점 가정하여 최대치 계산
        for (let i = lastIdx + 1; i < semesters.length; i++) {
            // 🚀 [수정] credits가 없으면 기본값 18학점 사용. 
            // 수동 입력된 값이 있으면 그 값을 사용하고, 없으면 18학점을 기본값으로 사용합니다.
            const c = semesterData[i].credits ?? 18; 
            
            // 4.5 만점 가정
            if (c > 0) {
                maxAchievableSum += c * 4.5;
                maxAchievableCreds += c;
            }
            
            // 최대 달성 가능 평점 업데이트
            projected[i] = maxAchievableCreds > 0 ? maxAchievableSum / maxAchievableCreds : null;
        }

        const data = {
            labels: semesters,
            datasets: [
                {
                    label: "각 학기 성적",
                    type: "bar",
                    data: semesterData.map((d) => d.gpa),
                    backgroundColor: "rgba(67, 178, 197, 0.6)",
                    borderColor: "rgba(2, 142, 167, 0.6)",
                    borderWidth: 1,
                    borderRadius: 5,
                    order: 2,
                },
                {
                    label: "누적/최대 평점",
                    type: "line",
                    data: projected,
                    borderColor: "#3B7F91",
                    backgroundColor: "transparent",
                    tension: 0.3,
                    borderWidth: 3,
                    pointRadius: 5,
                    pointBackgroundColor: "#3B7F91",
                    pointBorderColor: "#fff",
                    pointHoverRadius: 7,
                    order: 1,
                    // lastIdx까지는 실선, 그 이후부터는 점선
                    segment: {
                        borderDash: (ctx) => (ctx.p0DataIndex >= lastIdx ? [6, 6] : undefined),
                    },
                },
            ],
        };

        const currentVal =
            lastIdx >= 0 && data.datasets[1].data[lastIdx] != null
                ? Number(data.datasets[1].data[lastIdx])
                : null;
        const maxValRaw = data.datasets[1].data[data.datasets[1].data.length - 1];
        const maxVal = maxValRaw != null ? Number(maxValRaw) : null;

        return {
            chartData: data,
            lastFilledIndex: lastIdx,
            currentCumulativeGpaValue: currentVal,
            maxAchievableGpaValue: maxVal,
        };
    }, [semesterData]);

    const options = useMemo(
        () => ({
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: "top", labels: { usePointStyle: true, padding: 20 } },
                tooltip: {
                    callbacks: {
                        label: (ctx) => {
                            const raw = ctx.raw;
                            if (raw == null) return null;
                            const value = Number(raw).toFixed(2);
                            const isPast = ctx.dataIndex <= (ctx.chart?.$lastFilledIndex ?? -1);
                            
                            if (ctx.dataset.label === "누적/최대 평점") {
                                return isPast
                                    ? `누적 평점: ${value}점`
                                    : `달성 가능한 최대 평점: ${value}점`;
                            }
                            return `${ctx.dataset.label}: ${value}점`;
                        },
                    },
                },
            },
            scales: {
                x: { grid: { display: false }, title: { display: true, text: "학기" } },
                y: {
                    beginAtZero: true,
                    max: 4.5,
                    ticks: { stepSize: 0.5 },
                    title: { display: true, text: "평균 평점 (GPA)" },
                },
            },
        }),
        []
    );

    const onChartReady = (chart) => {
        // 차트 인스턴스에 lastFilledIndex를 전달하여 툴팁 콜백에서 사용할 수 있게 함
        if (chart) chart.$lastFilledIndex = lastFilledIndex;
    };

    return (
        <div className="bg-white rounded-2xl shadow-sm p-0 w-full h-full">
            <div className="px-6 py-4 border-b border-gray-200">
                <h2 className="text-xl font-semibold text-center">〈 나의 학점 로드맵 〉</h2>
            </div>

            <div className="w-full min-h-[320px] md:min-h-[360px] p-6">
                <Bar data={chartData} options={options} ref={onChartReady} />
            </div>

            <div className="border-t border-b border-gray-200 px-6 py-4">
                <div className="flex justify-center items-center gap-8 text-gray-700 text-base">
                    <div>
                        누적 평점:{" "}
                        {Number.isFinite(currentCumulativeGpaValue)
                            ? currentCumulativeGpaValue.toFixed(2)
                            : "-"}
                    </div>
                    <div>
                        달성 가능한 최대 평점:{" "}
                        {Number.isFinite(maxAchievableGpaValue)
                            ? maxAchievableGpaValue.toFixed(2)
                            : "-"}
                    </div>
                </div>
            </div>

            <div className="p-6">
                <h3 className="text-lg font-semibold text-center mb-4">
                    학기별 성적 입력 / 업로드
                </h3>

                <div className="flex flex-col items-center">
                    <div className="flex items-center gap-4 p-4 rounded-lg bg-gray-50 mb-2 w-full max-w-lg">
                        <label className="flex-shrink-0 text-gray-700 font-medium">
                            성적표 (PDF)
                        </label>
                        <input
                            ref={inputRef}
                            type="file"
                            accept=".pdf,application/pdf"
                            onChange={handleFileChange}
                            disabled={loading}
                            className="flex-grow block w-full text-sm text-gray-500
                             file:mr-4 file:py-2 file:px-4
                             file:rounded-full file:border-0
                             file:text-sm file:font-semibold
                             file:bg-cyan-50 file:text-cyan-700
                             hover:file:bg-cyan-100 disabled:opacity-60"
                        />
                    </div>
                    {selectedFile && (
                        <span className="text-sm text-gray-600 mb-2">
                            선택된 파일: {selectedFile.name}
                        </span>
                    )}
                    {loading && <span className="text-sm text-cyan-700">처리 중…</span>}
                    {err && <span className="text-sm text-red-500">{err}</span>}
                </div>

                <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-4">
                    {semesters.map((label, i) => (
                        <div
                            key={i}
                            className="flex flex-col items-center p-2 rounded-lg bg-gray-50"
                        >
                            <span className="font-medium text-sm mb-2">{label}</span>
                            <div className="flex flex-col space-y-2 w-full">
                                <input
                                    type="number"
                                    step="0.01"
                                    min="0"
                                    max="4.5"
                                    placeholder="평점 (4.5 만점)"
                                    value={semesterData[i].gpa ?? ""}
                                    onChange={(e) => {
                                        const v = e.target.value;
                                        const next = [...semesterData];
                                        next[i].gpa = v === "" ? null : parseFloat(v);
                                        setSemesterData(next);
                                    }}
                                    className="w-full text-center p-2 rounded border border-gray-300 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                />
                                <input
                                    type="number"
                                    min="0"
                                    placeholder="이수 학점"
                                    value={semesterData[i].credits ?? ""}
                                    onChange={(e) => {
                                        const v = e.target.value;
                                        const next = [...semesterData];
                                        next[i].credits = v === "" ? null : parseInt(v, 10);
                                        setSemesterData(next);
                                    }}
                                    className="w-full text-center p-2 rounded border border-gray-300 focus:outline-none focus:ring-2 focus:ring-cyan-500"
                                />
                            </div>
                        </div>
                    ))}
                </div>
            </div>

            <div className="border-t border-gray-200 px-6 py-3 text-center text-xs text-gray-500">
                ※ 성적이 없는 학기는 자동으로 비워두며, 필요 시 직접 입력할 수 있습니다.
            </div>
        </div>
    );
}