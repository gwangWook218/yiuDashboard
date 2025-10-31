import { Link } from "react-router-dom";
import yonginLogo from "/yongin-logo-white-transparent.png";

export default function GuestTopNav() {
  return (
    <header className="fixed top-0 left-0 right-0 z-50 bg-[#2F5664] text-white shadow-md">
      <div className="flex justify-between items-center px-6 h-14">
        {/* 왼쪽: 로고 */}
        <div className="flex items-center gap-3">
          <Link to="/" className="flex items-center gap-2">
            <img src={yonginLogo} alt="Yongin University Logo" className="h-8" />
          </Link>
        </div>

        {/* 오른쪽: 메뉴 */}
        <nav className="flex items-center gap-4">
          <a href="/#org" className="text-sm font-medium hover:underline">조직도 보기</a>
          <a href="/#parking" className="text-sm font-medium hover:underline">주차안내</a>
          <a href="/#map" className="text-sm font-medium hover:underline">캠퍼스맵</a>
        </nav>
      </div>
    </header>
  );
}
