import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import WelcomePage from "./pages/WelcomePage";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Guest from "./pages/Guest";
import StudentPage from "./pages/StudentPage";
import StaffPage from "./pages/StaffPage";
import AdminPage from "./pages/AdminPage";

function App() {
  return (
    <Router>
      <Routes>
        {/* 진입 */}
        <Route path="/" element={<WelcomePage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/guest" element={<Guest />} />

        {/* 역할별 페이지 */}
        <Route path="/student" element={<StudentPage />} />
        <Route path="/staff" element={<StaffPage />} />
        <Route path="/admin" element={<AdminPage />} />
      </Routes>
    </Router>
  );
}

export default App;