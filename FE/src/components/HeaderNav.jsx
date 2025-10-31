import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import yonginLogo from "/image.png";

export default function HeaderNav() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");
  const [userName, setUserName] = useState("");

  useEffect(() => {
    const storedId = localStorage.getItem("userId");
    const storedName = localStorage.getItem("userName");
    if (storedId) setUserId(storedId);
    if (storedName) setUserName(storedName);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem("userId");
    localStorage.removeItem("userName");
    localStorage.removeItem("userRole");
    navigate("/");
  };

  return (
    <header className="w-full bg-white border-b shadow-sm">
      <div className="flex justify-between items-center px-6 py-2 text-sm text-gray-700">
        {/* 로고 */}
        <div className="flex items-center space-x-6">
          <Link to="/">
            <img
              src={yonginLogo}
              alt="Yongin University"
              className="h-12 w-auto cursor-pointer"
            />
          </Link>
        </div>

        {/* 사용자 정보 */}
        <div className="flex items-center space-x-6">
          <span>
            학번:{" "}
            <span className="text-gray-800">
               {userId && `${userId}`}
            </span>
          </span>
          <button
            onClick={handleLogout}
            className="text-sm border px-2 py-1 rounded hover:bg-gray-100"
          >
            로그아웃
          </button>
        </div>
      </div>
    </header>
  );
}
