// src/pages/student.jsx
import React from "react";
import HeaderNav from "../components/HeaderNav";
import StudentTopNav from "../components/studentTopNav";

import PerformanceRoadmapGraph from "../components/02GPAanalysis";
import CreditProgressDetailCard from "../components/04CreditProgressCard";
import GraduateCareerChart from "../components/12GraduateCareerChart";
import GraduateCareerStatus from "../components/06GraduateCareerStatus";
import Footer from "../components/footer";

export default function Student() {
  return (
    <div className="min-h-screen flex flex-col bg-gray-100">
      <HeaderNav />
      <StudentTopNav />

      <main className="flex-1 w-full px-2 md:px-4 lg:px-6 py-4 lg:py-6">
        <div className="mx-auto w-full max-w-screen-2xl space-y-6">

          {/* 1행: 성적분석(8) + 학점상세(4) */}
          <section className="grid grid-cols-12 gap-6 items-stretch [grid-auto-rows:minmax(0,1fr)]">
            <div className="col-span-12 xl:col-span-8 h-full">
              <PerformanceRoadmapGraph />
            </div>
            <div className="col-span-12 xl:col-span-4 h-full">
              <CreditProgressDetailCard />
            </div>
          </section>

          <section>
              <GraduateCareerChart />
          </section>
          
          <section className="grid grid-cols-12 gap-6 [grid-auto-rows:minmax(0,1fr)]">
            <div className="col-span-12 h-full">
              <GraduateCareerStatus />
            </div>
          </section>

        </div>
      </main>

      <Footer />
    </div>
  );
}
