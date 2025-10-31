import React, { useEffect, useRef } from "react";

const loadKakao = (appKey) =>
  new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) return resolve(window.kakao);
    const script = document.createElement("script");
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${appKey}&autoload=false`;
    script.async = true;
    script.onload = () => {
      window.kakao.maps.load(() => resolve(window.kakao));
    };
    script.onerror = reject;
    document.head.appendChild(script);
  });

export default function KakaoMap({
  center = { lat: 37.2267, lng: 127.1682 },
  level = 3,
  facilities = [],
  height = 500,
  onSelect = () => {},
}) {
  const ref = useRef(null);
  const appKey = import.meta.env.VITE_KAKAO_APP_KEY;

  useEffect(() => {
    let map;
    let markers = [];
    (async () => {
      if (!appKey) return;
      const kakao = await loadKakao(appKey);
      const opts = {
        center: new kakao.maps.LatLng(center.lat, center.lng),
        level,
      };
      map = new kakao.maps.Map(ref.current, opts);

      // 마커 생성
      markers = facilities.map((f) => {
        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(f.lat, f.lng),
          map,
        });
        kakao.maps.event.addListener(marker, "click", () => onSelect(f));
        return marker;
      });
    })();
    return () => {
      markers.forEach((m) => m.setMap(null));
    };
  }, [center, level, facilities, appKey, onSelect]);

  if (!appKey) {
    return (
      <div className="flex items-center justify-center h-40 border rounded text-sm text-gray-500">
        .env에 VITE_KAKAO_APP_KEY를 설정하세요
      </div>
    );
  }

  return <div ref={ref} style={{ width: "100%", height }} />;
}