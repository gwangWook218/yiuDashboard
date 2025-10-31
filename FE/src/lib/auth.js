// src/lib/auth.js

export const API_URL = (import.meta.env.VITE_API_URL || "").replace(/\/$/, "");
export const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";

/* ───────────────── 공통 유틸 ───────────────── */
function isCrossOrigin(base) {
  if (!base) return false;
  try {
    const apiOrigin = new URL(base, location.origin).origin;
    return apiOrigin !== location.origin;
  } catch {
    return false;
  }
}
const CROSS = isCrossOrigin(API_URL);
// 교차출처면 credentials를 빼서 프리플라이트/쿠키 이슈 최소화
const DEFAULT_CREDENTIALS = CROSS ? "omit" : "include";

async function readErrorMessage(res) {
  const ct = (res.headers.get("content-type") || "").toLowerCase();
  const text = await res.text().catch(() => "");
  if (ct.includes("application/json")) {
    try {
      const j = JSON.parse(text || "{}");
      return j.message || j.error || JSON.stringify(j);
    } catch {}
  }
  return text || `HTTP ${res.status}`;
}

function authHeaders(extra = {}) {
  const token = localStorage.getItem("accessToken");
  return {
    Accept: "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...extra,
  };
}

// JSON이 꼭 필요하지 않은 POST는 URLSearchParams로 보내 “간단 요청”으로 만든다.
function toBody(body) {
  if (!body || typeof body !== "object") return body;
  // 이미 FormData/URLSearchParams면 그대로
  if (body instanceof FormData || body instanceof URLSearchParams) return body;
  // 교차출처면 URLSearchParams로 변환
  if (CROSS) {
    const sp = new URLSearchParams();
    for (const [k, v] of Object.entries(body)) {
      if (v !== undefined && v !== null) sp.append(k, String(v));
    }
    return sp;
  }
  // 동출처면 JSON
  return JSON.stringify(body);
}

async function http(method, url, body) {
  const isJson =
    !CROSS &&
    body != null &&
    !(body instanceof FormData) &&
    !(body instanceof URLSearchParams);

  const res = await fetch(url, {
    method,
    headers:
      body != null
        ? authHeaders(isJson ? { "Content-Type": "application/json" } : {})
        : authHeaders(),
    body: body != null ? toBody(body) : undefined,
    credentials: DEFAULT_CREDENTIALS,
    mode: "cors",
  });

  // 에러 응답이면 메시지 읽어서 throw
  if (!res.ok) {
    let msg = await readErrorMessage(res);
    if (res.status === 401 || res.status === 403) {
      msg = "아이디 또는 비밀번호가 올바르지 않습니다.";
    } else if (res.status >= 500) {
      msg = "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
    }
    const err = new Error(msg);
    err.status = res.status;
    throw err;
  }

  // ✅ 성공 응답 파싱: JSON 아니면 { message: text } 로 감싸서 반환
  const ct = (res.headers.get("content-type") || "").toLowerCase();
  const text = await res.text();
  try {
    if (ct.includes("application/json")) {
      return JSON.parse(text || "{}");
    }
  } catch {
    // 조용히 텍스트로 처리
  }
  return { message: text };
}

/* ───────────────── 역할 정규화 ───────────────── */
export function normalizeRole(role) {
  const r = String(role || "").toLowerCase();
  if (r === "student") return "STUDENT";
  if (r === "staff" || r === "professor" || r === "employee") return "PROFESSOR";
  return r.toUpperCase() || "STUDENT";
}

/* ───────────────── CSRF 프라임(선택) ───────────────── */
export async function primeCsrf() {
  try {
    const url = `${API_URL}/api/public/main/students?year=2024`;
    await fetch(url, { credentials: DEFAULT_CREDENTIALS });
  } catch {}
}

/* ───────────────── 로그인 ───────────────── */
export async function loginRequest(payload) {
    try {
        if (USE_MOCK) {
            await new Promise((r) => setTimeout(r, 300));
            const idMock = payload.loginId ?? payload.id ?? "user";
            return {
                accessToken: "mock-access-token",
                refreshToken: "mock-refresh-token",
                user: { id: idMock, role: normalizeRole(payload.role) || "STUDENT" },
                raw: { mock: true },
            };
        }

        const base = API_URL;
        const id = payload.loginId ?? payload.id;
        const password = payload.password ?? payload.pw;
        if (!id || !password) throw new Error("아이디/비밀번호가 비었습니다.");

        const url = `${base}/api/auth/login`;
        const resp = await http("POST", url, { loginId: id, password });

        // 1. JWT 추출
        const accessToken = resp?.accessToken || resp?.token || resp?.jwt;
        const refreshToken = resp?.refreshToken;

        // 2. JWT에서 권한 추출 (예: auth claim)
        let role = "student"; // 기본값
        if (accessToken) {
            try {
                const payloadBase64 = accessToken.split(".")[1];
                const decoded = JSON.parse(atob(payloadBase64));
                if (decoded.auth) {
                    // JWT에 auth가 "ROLE_PROFESSOR,ROLE_STUDENT" 같이 있을 경우
                    const roles = String(decoded.auth).split(",").map(r => r.replace(/^ROLE_/, "").toUpperCase());
                    role = roles.includes("PROFESSOR") ? "PROFESSOR" : "STUDENT";
                }
            } catch (e) {
                console.warn("JWT 파싱 실패:", e);
            }
        }

        return {
            accessToken,
            refreshToken,
            user: { id, role },
            raw: resp,
        };
    } catch (e) {
        throw e || new Error("로그인 실패");
    }
}

/* ───────────────── 아이디 중복확인 ───────────────── */
// 백엔드가 JSON {message, exists} 로 응답 → 심플하게 GET 하나만 사용
// src/lib/auth.js
export async function checkIdAvailability(loginId) {
  if (USE_MOCK) {
    await new Promise((r) => setTimeout(r, 150));
    return { exists: String(loginId) === "takenId", raw: { mock: true } };
  }

  const base = API_URL;
  const clean = String(loginId || "").trim();
  if (!clean) throw new Error("loginId가 비었습니다.");

  const url = `${base}/api/auth/check-id?loginId=${encodeURIComponent(clean)}`;

  try {
    const r = await http("GET", url); // 200 OK → 사용 가능
    // 백엔드가 {exists:false, message:"..."} 형식이면 그대로 활용
    const exists = r?.exists ?? false;
    const message = r?.message || "사용 가능한 ID입니다.";
    return { exists, message, raw: r };
  } catch (e) {
    // ✅ 409는 “이미 사용 중”으로 정상 처리 (팝업 X)
    if (e.status === 409) {
      return { exists: true, message: e.message || "이미 사용 중인 ID입니다." };
    }
    // 그 외(500/네트워크 등)는 진짜 예외
    throw e;
  }
}

/* ───────────────── 회원가입 ───────────────── */
// /api/auth/register 한 경로만 JSON으로 호출
export async function signupRequest({ loginId, password, email, role, passwordCheck }) {
  if (USE_MOCK) {
    await new Promise((r) => setTimeout(r, 300));
    return { ok: true, mock: true };
  }

  const base = API_URL;
  const normRole = normalizeRole(role);
  const url = `${base}/api/auth/signup`;
  const payload = { loginId, password, passwordCheck, email, role: normRole };

  return await http("POST", url, payload);
}

/* ───────────────── 세션 헬퍼 ───────────────── */
export function persistSession(payload, remember = true) {
  const store = remember ? localStorage : sessionStorage;

  const userId = payload.user?.id || payload.loginId || payload.id;
  const userRole = payload.user?.role || payload.role;

  if (userId) store.setItem("userId", userId);
  if (userRole) store.setItem("userRole", userRole);
  if (payload.accessToken) store.setItem("accessToken", payload.accessToken);
  if (payload.refreshToken) store.setItem("refreshToken", payload.refreshToken);
}

export function clearSession() {
  for (const k of ["userId", "userRole", "accessToken", "refreshToken"]) {
    try { localStorage.removeItem(k); } catch {}
    try { sessionStorage.removeItem(k); } catch {}
  }
}

/* ───────────────── 내 정보(옵션) ───────────────── */
export async function fetchMe() {
  const base = API_URL;
  const paths = [`${base}/api/auth/me`, `${base}/api/users/me`, `${base}/api/user/me`];
  let lastErr;
  for (const p of paths) {
    try { return await http("GET", p); } catch (e) { lastErr = e; }
  }
  throw lastErr || new Error("내 정보 조회 실패");
}
