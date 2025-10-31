import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Guest from "./pages/Guest";
import Signup from "./pages/Signup";
import Student from "./pages/student";  
import Findid from "./pages/Findid";
import Employees from "./pages/Employees";
import Report from "./pages/Report";
import Faculty from "./pages/Faculty";

import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Guest/>} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/student" element={<Student />} />
        <Route path="/findid" element={<Findid />} />
        <Route path="/Employees" element={<Employees />} />
        <Route path="/Report" element={<Report />} />
        <Route path="/Faculty" element={<Faculty />} />
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
