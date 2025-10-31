// src/pages/Report.jsx
import React from "react";
import StaffHeaderNav from "../components/StaffHeaderNav";
import StaffTopNav from "../components/StaffTopNav";

import UniCompareKPI from "../components/18UniCompareKPI";
import GraduateCareerChart from "../components/12GraduateCareerChart";
import FacultyRatioCard from "../components/14FacultyRatioCard";
import FacultyTeachingRatio from "../components/15FacultyTeachingRatio";
import GraduateCareerStatus from "../components/06GraduateCareerStatus";
import Footer from "../components/Footer";

export default function Faculty() {
  return (
    <div className="min-h-screen flex flex-col bg-gray-100">
      <StaffHeaderNav />
      <StaffTopNav />

      <main className="flex-1 w-full px-2 md:px-4 lg:px-6 py-6">
        <div className="mx-auto w-full max-w-screen-2xl space-y-6">
          <section>
            <UniCompareKPI  />
          </section>

          <section>
            <GraduateCareerChart />
          </section>

          <section>
            <FacultyRatioCard />
          </section>

          <section>
            <FacultyTeachingRatio />
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

