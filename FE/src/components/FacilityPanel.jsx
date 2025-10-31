import React, { useRef } from "react";

function Slider({ details = [] }) {
  const [idx, setIdx] = React.useState(0);
  if (!details.length) return null;

  const prev = () => setIdx(i => (i - 1 + details.length) % details.length);
  const next = () => setIdx(i => (i + 1) % details.length);
  const current = details[idx];

  return (
    <figure className="w-full">
      <div className="relative w-full aspect-[16/9] overflow-hidden rounded-md mb-2">
        <img src={current.img} alt="facility" className="absolute inset-0 w-full h-full object-cover" />
        <button onClick={prev} className="absolute left-2 top-1/2 -translate-y-1/2 bg-black/50 text-white text-xs rounded px-2 py-1">〈</button>
        <button onClick={next} className="absolute right-2 top-1/2 -translate-y-1/2 bg-black/50 text-white text-xs rounded px-2 py-1">〉</button>
      </div>
      {current.desc && <figcaption className="text-sm text-gray-700 leading-relaxed">{current.desc}</figcaption>}
    </figure>
  );
}

export default function FacilityPanel({ selected }) {
  const infoRef = useRef(null);
  const amenRef = useRef(null);
  const scrollTo = (ref) => ref.current?.scrollIntoView({ behavior: "smooth", inline: "start", block: "nearest" });

  return (
    <aside className="bg-white rounded-xl shadow-sm border border-gray-300 overflow-hidden h-[500px]">
      {selected ? (
        <div className="h-full flex flex-col">
          {/* 헤더 (구분선 있음) */}
          <div className="p-4 border-b border-gray-200">
            <h3 className="text-base font-semibold text-gray-800">{selected.name}</h3>
          </div>

          {/* 본문 */}
          <div className="flex-1 min-h-0 overflow-x-auto snap-x snap-mandatory flex no-scrollbar">
            {/* 슬라이드 1: 정보 */}
            <div ref={infoRef} className="min-w-full snap-start flex flex-col">
              <div className="p-4 space-y-3 overflow-y-auto">
                {selected.info?.image && (
                  <div className="w-full aspect-[16/9] overflow-hidden rounded-md">
                    <img src={selected.info.image} alt={selected.name} className="w-full h-full object-cover" />
                  </div>
                )}

                {selected.info?.desc && <p className="text-sm text-gray-700">{selected.info.desc}</p>}

                {typeof selected.info?.instagram === "string" && selected.info.instagram.trim() && (
                  <a
                    href={
                      selected.info.instagram.startsWith("http")
                        ? selected.info.instagram
                        : `https://www.instagram.com/${selected.info.instagram.replace(/^@/, "")}`
                    }
                    target="_blank"
                    rel="noopener noreferrer"
                    aria-label="Instagram으로 이동"
                    className="inline-flex items-center gap-2 rounded-full px-3 py-1.5 text-sm font-medium text-rose-600 bg-rose-50 hover:bg-rose-100 transition"
                  >
                    <svg viewBox="0 0 24 24" width="16" height="16" aria-hidden="true" className="opacity-90">
                      <path fill="currentColor" d="M7 2h10a5 5 0 0 1 5 5v10a5 5 0 0 1-5 5H7a5 5 0 0 1-5-5V7a5 5 0 0 1 5-5zm10 2H7a3 3 0 0 0-3 3v10a3 3 0 0 0 3 3h10a3 3 0 0 0 3-3V7a3 3 0 0 0-3-3zm-5 4a5 5 0 1 1 0 10 5 5 0 0 1 0-10zm0 2.2A2.8 2.8 0 1 0 12 16.8 2.8 2.8 0 0 0 12 10.2zM17.5 6a1.1 1.1 0 1 1 0 2.2 1.1 1.1 0 0 1 0-2.2z"/>
                    </svg>
                    {selected.info.instagramLabel ? selected.info.instagramLabel : `@${selected.info.instagram.replace(/^@/, "")}`}
                  </a>
                )}
              </div>
            </div>

            {/* 슬라이드 2: 편의시설 */}
            <div ref={amenRef} className="min-w-full snap-start flex flex-col">
              <div className="p-4 flex-1 min-h-0 flex flex-col">
                {Array.isArray(selected.amenities) && selected.amenities.length ? (
                  <div className="flex-1 min-h-0">
                    <Slider details={selected.amenities.map(a => ({ img: a.img, desc: a.desc }))} />
                  </div>
                ) : (
                  <div className="flex-1 min-h-0 flex items-center justify-center text-sm text-gray-500">
                    등록된 편의시설이 없습니다.
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* 푸터 (구분선 있음) */}
          <div className="p-3 border-t border-gray-200 flex items-center justify-center gap-2">
            <button onClick={() => scrollTo(infoRef)} className="px-3 py-1.5 text-xs rounded-lg bg-gray-100 hover:bg-gray-300">건물 정보</button>
            <button onClick={() => scrollTo(amenRef)} className="px-3 py-1.5 text-xs rounded-lg bg-gray-100 hover:bg-gray-300">편의시설</button>
          </div>
        </div>
      ) : (
        <div className="p-4 text-sm text-gray-500">마커를 클릭하면 시설 정보가 표시됩니다.</div>
      )}
    </aside>
  );
}
