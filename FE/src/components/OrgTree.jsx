import React, { useRef, useState, useLayoutEffect } from "react";

// 최상위 children 순서: 부총장이 가운데 오게!
export const orgData = {
  name: "총장",
  title: "President",
  children: [
    { name: "감사실" },
    {
      name: "부총장",
      title: "Vice President",
      children: [
        {
          name: "대학본부",
          children: [
            { name: "기획처" }, { name: "교무처" }, { name: "교육혁신처" }, { name: "학생처" },
            { name: "사무처" }, { name: "국제교류교육원" }, { name: "입학관리실" }, { name: "기관생명윤리위원회" }
          ]
        },
        {
          name: "대학",
          children: [
            { name: "무도대학", children: [{ name: "유도학과" }, { name: "유도경기지도학과" }, { name: "무도학과" }, { name: "태권도학과" }, { name: "경호학과" }] },
            { name: "체육과학대학", children: [{ name: "스포츠레저학과" }, { name: "체육학과" }, { name: "골프학과" }, { name: "특수체육교육과" }] },
            { name: "문화예술대학", children: [{ name: "무용과" }, { name: "미디어디자인학과" }, { name: "회화학과" }, { name: "연극학과" }, { name: "국악과" }, { name: "영화영상학과" }, { name: "문화유산학과" }, { name: "문화콘텐츠학과" }, { name: "실용음악과" }] },
            { name: "인문사회융합대학", children: [{ name: "경영학과" }, { name: "관광경영학과" }, { name: "경찰행정학과" }, { name: "중국학과" }, { name: "사회복지학과" }] },
            { name: "AI바이오융합대학", children: [{ name: "보건환경안전학과" }, { name: "바이오생명공학과" }, { name: "물리치료학과" }, { name: "식품조리학부" }, { name: "AI융합학부" }] },
            { name: "용오름대학", children: [{ name: "교양 지원과" }] }
          ]
        },
        {
          name: "대학원",
          children: [
            { name: "대학원[박사과정]" }, { name: "대학원[석사과정]" }, { name: "스포츠과학대학원" }, { name: "경영대학원" },
            { name: "교육대학원" }, { name: "문화예술대학원" }, { name: "재활복지대학원" }, { name: "태권도대학원" }
          ]
        },
        {
          name: "부속기관",
          children: [
            { name: "중앙도서관" }, { name: "생활관" }, { name: "체육지원실" }, { name: "신문방송국" }, { name: "박물관" },
            { name: "산학협력단" }, { name: "스포츠 & 웰니스연구센터" }, { name: "정보관리실" }, { name: "예비군대대" }, { name: "글로벌사회공헌원" }
          ]
        },
        {
          name: "부설기관",
          children: [
            { name: "생활체육지도자연수원" }, { name: "미래인재교육원" }, { name: "교육연수원" }, { name: "장애인스포츠지도자연수원" },
            { name: "학생군사교육단" }, { name: "무도연구소" }, { name: "체육과학연구소" }, { name: "특수체육연구소" },
            { name: "자연과학연구소" }, { name: "인문사회과학연구소" }, { name: "문화예술연구소" }, { name: "인권센터" }
          ]
        }
      ]
    },
    { name: "비서실" }
  ]
};


function OrgNode({ node, level = 0, lineColor = "#2F5664", expandAll, measureNonce = 0 }) {
  const hasChildren = Array.isArray(node.children) && node.children.length > 0;
  const [expanded, setExpanded] = useState(level === 0);

  React.useEffect(() => {
    if (expandAll === true) setExpanded(true);
    else if (expandAll === false) setExpanded(level === 0);
  }, [expandAll, level]);

  const wrapRef = useRef(null);
  const childRefs = useRef([]);
  const [centers, setCenters] = useState([]);
  const [w, setW] = useState(0);

  const measure = React.useCallback(() => {
    const container = wrapRef.current;
    if (!container) return;
    const els = childRefs.current.filter(Boolean);
    if (!els.length) {
      setCenters([]);
      setW(container.scrollWidth || container.clientWidth || 0);
      return;
    }
    const cs = els.map((el) => el.offsetLeft + el.offsetWidth / 2);
    setCenters(cs);
    setW(container.scrollWidth || container.clientWidth || 0);
  }, []);

  useLayoutEffect(() => {
    if (!expanded || !hasChildren) return;
    let af1 = 0, af2 = 0;
    af1 = requestAnimationFrame(() => { af2 = requestAnimationFrame(measure); });
    return () => { cancelAnimationFrame(af1); cancelAnimationFrame(af2); };
  }, [expanded, hasChildren, node.children?.length, measure, measureNonce]);

  useLayoutEffect(() => {
    if (!expanded || !hasChildren || !wrapRef.current) return;
    const container = wrapRef.current;
    let pending = false;
    const onResize = () => {
      if (pending) return;
      pending = true;
      requestAnimationFrame(() => { measure(); pending = false; });
    };
    const ro = new ResizeObserver(onResize);
    ro.observe(container);
    childRefs.current.forEach((el) => el && ro.observe(el));
    return () => ro.disconnect();
  }, [expanded, hasChildren, measure]);

  const nodeStyle = {
    padding: level === 0 ? "12px 20px" : "8px 14px",
    backgroundColor: level === 0 ? "#2F5664" : "#f0f4f8",
    color: level === 0 ? "white" : "#2F5664",
    borderRadius: 12,
    boxShadow: level === 0 ? "0 6px 14px rgba(0,0,0,0.12)" : "0 1px 0 rgba(0,0,0,0.04)",
    cursor: hasChildren ? "pointer" : "default",
    textAlign: "center",
    minWidth: 140,
    position: "relative",
  };

  return (
    <div style={{ display: "flex", flexDirection: "column", alignItems: "center", marginTop: 16 }}>
      <div style={nodeStyle} onClick={() => hasChildren && setExpanded((p) => !p)}>
        <p className="font-bold">{node.name}</p>
        {node.title && <p className="text-sm opacity-90">{node.title}</p>}
        {hasChildren && (
          <p className="text-xs mt-1" style={{ color: "#6b7280" }}>
            {expanded ? "클릭해서 접기 ▲" : "클릭해서 펼치기 ▼"}
          </p>
        )}
      </div>
      {expanded && hasChildren && (
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", marginTop: 24, width: "100%" }}>
          <div style={{ position: "relative", width: "100%" }}>
            <svg
              style={{ width: w, height: 40, position: "absolute", top: 0, left: 0, overflow: "visible" }}
              width={w}
              height={40}
            >
              {centers.length > 0 && (
                <>
                  <line x1={w / 2} y1={0} x2={w / 2} y2={16} stroke={lineColor} strokeWidth={3} strokeLinecap="round" />
                  {centers.length === 1 ? (
                    <>
                      <line x1={w / 2} y1={16} x2={centers[0]} y2={16} stroke={lineColor} strokeWidth={3} strokeLinecap="round" />
                      <line x1={centers[0]} y1={16} x2={centers[0]} y2={40} stroke={lineColor} strokeWidth={3} strokeLinecap="round" />
                    </>
                  ) : (
                    <>
                      <line x1={Math.min(...centers)} y1={16} x2={Math.max(...centers)} y2={16} stroke={lineColor} strokeWidth={3} strokeLinecap="round" />
                      {centers.map((cx, i) => (
                        <line key={i} x1={cx} y1={16} x2={cx} y2={40} stroke={lineColor} strokeWidth={3} strokeLinecap="round" />
                      ))}
                    </>
                  )}
                </>
              )}
            </svg>
            <div
              ref={wrapRef}
              style={{
                display: "flex",
                gap: 16,
                justifyContent: "center",
                alignItems: "flex-start",
                marginTop: 40,
                width: "100%",
                overflowX: "auto",
                flexWrap: "nowrap",
                position: "relative",
              }}
            >
              {node.children.map((child, i) => (
                <div key={i} ref={(el) => (childRefs.current[i] = el)}>
                  <OrgNode node={child} level={level + 1} lineColor={lineColor} expandAll={expandAll} measureNonce={measureNonce} />
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export function UniversityOrgChartCard() {
  const [expandAll, setExpandAll] = useState(null);
  const [measureNonce, setMeasureNonce] = useState(0);

  const chartWrapRef = useRef(null);
  const orgChartRef = useRef(null);
  const [scale, setScale] = useState(1);

  const applyExpand = (v) => {
    setExpandAll(v);
    requestAnimationFrame(() => {
      requestAnimationFrame(() => setMeasureNonce((n) => n + 1));
    });
  };

  useLayoutEffect(() => {
    if (expandAll !== true) {
      setScale(1);
      return;
    }
    const cardWidth = chartWrapRef.current?.clientWidth || 0;
    const orgWidth = orgChartRef.current?.scrollWidth || orgChartRef.current?.clientWidth || 0;
    if (cardWidth > 0 && orgWidth > cardWidth) {
      const newScale = Math.max(cardWidth / orgWidth, 0.65);
      setScale(newScale);

      // scale 적용 후 중앙으로 스크롤 이동
      requestAnimationFrame(() => {
        if (chartWrapRef.current)
          chartWrapRef.current.scrollLeft =
            (chartWrapRef.current.scrollWidth - chartWrapRef.current.clientWidth) / 2;
      });
    } else {
      setScale(1);
    }
  }, [expandAll, measureNonce]);

  return (
    <div className="bg-white rounded-2xl shadow-sm p-6 border border-gray-200">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-bold text-[#2F5664]">대학 조직도</h2>
        <div className="space-x-2">
          <button onClick={() => applyExpand(true)} className="px-3 py-1 bg-[#2F5664] text-white rounded-md text-sm">전체 펼치기</button>
          <button onClick={() => applyExpand(false)} className="px-3 py-1 bg-gray-300 text-sm rounded-md">전체 접기</button>
        </div>
      </div>
      <div
        ref={chartWrapRef}
        className="overflow-auto"
        style={{
          width: "100%",
          maxHeight: "70vh",
          minHeight: 200,
          boxSizing: "border-box"
        }}
      >
        <div
          ref={orgChartRef}
          style={{
            transform: `scale(${scale})`,
            transformOrigin: "top center",
            transition: "transform 0.4s cubic-bezier(.25,.8,.25,1)",
            width: "fit-content",
            minWidth: "100%",
            margin: "0 auto"
          }}
        >
          <OrgNode node={orgData} expandAll={expandAll} measureNonce={measureNonce} />
        </div>
      </div>
    </div>
  );
}
