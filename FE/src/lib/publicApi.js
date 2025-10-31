// src/lib/publicApi.js
// JSON + XML + text 모두 처리

function buildURL(path, params = {}) {
  const url = new URL(path, window.location.origin);
  Object.entries(params || {}).forEach(([k, v]) => {
    if (v !== undefined && v !== null && v !== "") url.searchParams.set(k, v);
  });
  return url.toString();
}

function toNumMaybe(s) {
  if (typeof s !== "string") return s;
  const n = Number(s.replace(/[, ]/g, ""));
  return Number.isFinite(n) ? n : s;
}

function parseXML(text) {
  try {
    const parser = new DOMParser();
    const xml = parser.parseFromString(text, "application/xml");
    if (xml.querySelector("parsererror")) return { _text: text };
    const item = xml.querySelector("item") || xml.documentElement;
    const obj = {};
    item.childNodes.forEach((n) => {
      if (n.nodeType === 1) {
        const key = n.nodeName;
        const val = (n.textContent || "").trim();
        obj[key] = toNumMaybe(val);
      }
    });
    return Object.keys(obj).length ? obj : { _text: text };
  } catch {
    return { _text: text };
  }
}

/* ───────── 공개 API ───────── */
export async function getPublic(path, params) {
  const url = buildURL(path, params);
  const res = await fetch(url, { headers: { Accept: "*/*" } });
  const contentType = res.headers.get("content-type") || "";
  const text = await res.text();

  if (!res.ok) throw new Error(`[${res.status}] ${text.slice(0, 200)}`);

  if (contentType.includes("application/json")) {
    try { return JSON.parse(text); } catch { return { _text: text }; }
  }
  if (contentType.includes("xml") || text.trim().startsWith("<")) return parseXML(text);

  const mValue = text.match(/<value>\s*([0-9][0-9, .]*)\s*<\/value>/i);
  if (mValue) {
    const n = Number(String(mValue[1]).replace(/[, ]/g, ""));
    if (Number.isFinite(n)) return { value: n };
  }
  const mCount = text.match(/<count>\s*([0-9][0-9, .]*)\s*<\/count>/i);
  if (mCount) {
    const n = Number(String(mCount[1]).replace(/[, ]/g, ""));
    if (Number.isFinite(n)) return { count: n };
  }
  return { _text: text };
}

/* ───────── 인증 필요한 API (토큰/세션 포함) ───────── */
function authHeaders(extra = {}) {
  const token = localStorage.getItem("accessToken"); // 로그인 시 저장된 토큰
  return {
    Accept: "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...extra,
  };
}

export async function getAuth(path, params) {
  const url = buildURL(path, params);
  const res = await fetch(url, {
    headers: authHeaders(),
    credentials: "include", // 세션 쿠키 쓰는 백엔드 대비(무해)
  });
  const ct = (res.headers.get("content-type") || "").toLowerCase();
  const text = await res.text();
  if (!res.ok) throw new Error(`HTTP ${res.status}`);

  if (ct.includes("application/json")) {
    try { return JSON.parse(text); } catch { /* fallthrough */ }
  }
  return text; // 필요 시 caller에서 처리
}

export async function postAuth(path, body) {
  const res = await fetch(path, {
    method: "POST",
    headers: authHeaders({ "Content-Type": "application/json" }),
    body: JSON.stringify(body ?? {}),
    credentials: "include",
  });
  const ct = (res.headers.get("content-type") || "").toLowerCase();
  const text = await res.text();
  if (!res.ok) throw new Error(`HTTP ${res.status}: ${text.slice(0, 200)}`);

  if (ct.includes("application/json")) {
    try { return JSON.parse(text); } catch { /* fallthrough */ }
  }
  return text;
}
