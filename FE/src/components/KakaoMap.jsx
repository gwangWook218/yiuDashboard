import React, { useEffect, useRef } from "react";

// Kakao SDK 로드 함수
const loadKakao = (appKey) =>
  new Promise((resolve, reject) => {
    if (window.kakao && window.kakao.maps) return resolve(window.kakao);

    const script = document.createElement("script");
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${appKey}&autoload=false`;
    script.async = true;

    script.onload = () => {
      if (!window.kakao) return reject(new Error("Kakao SDK not loaded"));
      window.kakao.maps.load(() => {
        if (!window.kakao.maps) return reject(new Error("Kakao Maps not available"));
        resolve(window.kakao);
      });
    };

    script.onerror = () => reject(new Error("Kakao SDK load failed"));
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

    const initMap = async () => {
      if (!appKey) return console.warn("VITE_KAKAO_APP_KEY가 설정되지 않았습니다.");

      try {
        const kakao = await loadKakao(appKey);

        // 지도 옵션
        const options = {
          center: new kakao.maps.LatLng(center.lat, center.lng),
          level,
        };

        // 지도 생성
        map = new kakao.maps.Map(ref.current, options);

        // 마커 생성
        markers = facilities.map((f) => {
          const marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(f.lat, f.lng),
            map,
          });
          kakao.maps.event.addListener(marker, "click", () => onSelect(f));
          return marker;
        });
      } catch (err) {
        console.error("Kakao Map Load Error:", err);
      }
    };

    initMap();

    return () => {
      // 컴포넌트 언마운트 시 마커 제거
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
