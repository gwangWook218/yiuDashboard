import React from "react";
import LoginCard from "./LoginCard";

export default function HeroHeader() {
  return (
    <div className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-[#2F5664] to-[#3f6d7e] text-white p-6 md:p-8 shadow">
      <div
        className="pointer-events-none absolute inset-0 opacity-15"
        style={{
          backgroundImage:
            "radial-gradient(transparent 0, transparent 10px, rgba(255,255,255,.12) 10px, rgba(255,255,255,.12) 11px)",
          backgroundSize: "24px 24px",
        }}
      />
      <div className="relative z-10 grid md:grid-cols-2 gap-8 items-center">
        <div className="space-y-3">
          <p className="text-xs md:text-sm text-white/70">YONGIN UNIVERSITY DASHBOARD</p>
          <h1 className="text-2xl md:text-4xl font-extrabold leading-tight">용인대학교 현황판</h1>
          <p className="text-sm md:text-base text-white/85 max-w-md">
            학생 / 교직원 서비스를 이용하려면 로그인하세요.
          </p>
        </div>
        <div className="flex md:justify-end">
          <LoginCard />
        </div>
      </div>
    </div>
  );
}