// src/pages/Employees.jsx
import React from "react";

import StaffHeaderNav from "../components/staffHeaderNav";
import StaffTopNav from "../components/staffTopNav";

import DropoutKPI from "../components/17DropoutKPI";
import Footer from "../components/footer";
import GraduateCareerStatus from "../components/06GraduateCareerStatus";

export default function Employees() {
  return (
    <div className="min-h-screen flex flex-col bg-gray-100">
      <StaffHeaderNav />
      <StaffTopNav />

      <main className="flex-1 w-full px-6 lg:px-10 xl:px-14 py-8">
        {/* 최대 폭 + 위아래 여백 */}
        <div className="mx-auto max-w-screen-2xl space-y-6">
          {/* 각 KPI 카드 */}
          <section>
            <DropoutKPI />
          </section>

          <section>
            <GraduateCareerStatus />
          </section>


        </div>
      </main>

      <Footer />
    </div>
  );
}
