// src/pages/Faculty.jsx
import React from "react";
import StaffHeaderNav from "../components/staffHeaderNav";
import StaffTopNav from "../components/staffTopNav";

import FacultySecuredStatus from "../components/14.5FacultySecuredStatus";
import ScholarshipKPI from "../components/19ScholarshipKPI";
import FacultyResearchExpensePerFaculty from "../components/16.5FacultyResearchExpensePerFaculty";
import GraduateCareerStatus from "../components/06GraduateCareerStatus";
import Footer from "../components/footer";

export default function Faculty() {
  return (
    <div className="min-h-screen flex flex-col bg-gray-100">
      <StaffHeaderNav />
      <StaffTopNav />

      <main className="flex-1 w-full px-2 md:px-4 lg:px-6 py-6">
        <div className="mx-auto w-full max-w-screen-2xl space-y-6">

          <section>
            <ScholarshipKPI />
          </section>

          <section>
            <FacultyResearchExpensePerFaculty />
          </section>

          <section>
            <FacultySecuredStatus />
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
