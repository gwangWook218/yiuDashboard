import React, { useEffect, useState } from "react";
import { getPublic } from "../lib/publicApi";
import AdmissionChart from "./AdmissionChart";

const YEAR_DEFAULT = 2024;

export default function AdmissionChartContainer() {
  const [data, setData] = useState(null);
  const [err, setErr] = useState("");

  useEffect(() => {
    (async () => {
      try {
        // 백엔드 API: /api/public/main/recruitment?year=2024
        const res = await getPublic("/api/public/main/recruitment", { year: YEAR_DEFAULT });
        setData(res);
      } catch (e) {
        setErr(e.message ?? String(e));
      }
    })();
  }, []);

  if (err) return <div className="text-red-600">입시 데이터 불러오기 실패: {err}</div>;
  if (!data) return <div>로딩 중...</div>;

  // AdmissionChart가 props.data를 받을 수 있게 되어 있다면
  return <AdmissionChart data={data} />;
}
