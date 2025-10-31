// src/lib/studentApi.js
export async function getGraduateEmployment({ year, dept }) {
  const res = await fetch(`/api/student/graduates/employment?year=${year}&dept=${dept}`, {
    credentials: "include",
  });
  if (!res.ok) throw new Error("취업 현황 불러오기 실패");
  return res.json();
}


export async function uploadSemesterPdf(formData) {
  const res = await fetch(`/api/grades/semester/upload`, {
    method: "POST",
    body: formData,
    credentials: "include",
  });
  if (!res.ok) throw new Error("학기별 성적 PDF 업로드 실패");
  return res.json();
}

export async function getSemesterGrades() {
  const res = await fetch(`/api/grades/semester`, { credentials: "include" });
  if (!res.ok) throw new Error("학기별 성적 조회 실패");
  return res.json();
}

export async function uploadCreditProgressPdf(formData) {
  const res = await fetch(`/api/grades/credit-progress/upload`, {
    method: "POST",
    body: formData,
    credentials: "include",
  });
  if (!res.ok) throw new Error("이수구분별 성적 PDF 업로드 실패");
  return res.json();
}

export async function getCreditProgress() {
  const res = await fetch(`/api/grades/credit-progress`, { credentials: "include" });
  if (!res.ok) throw new Error("이수구분별 성적 조회 실패");
  return res.json();
}

export async function saveStudentInfo(payload) {
  const res = await fetch(`/api/grades/student-info/save`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
    credentials: "include",
  });
  if (!res.ok) throw new Error("추가 학업정보 저장 실패");
  return res.json();
}

export async function getMyAdditionalInfo() {
  const res = await fetch(`/api/grades/my-info-add`, { credentials: "include" });
  if (!res.ok) throw new Error("추가 학업정보 조회 실패");
  return res.json();
}
