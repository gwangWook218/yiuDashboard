import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="bg-white mt-10 py-6 text-gray-600 text-xs">
      <div className="max-w-6xl mx-auto px-4 flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
        <div className="flex flex-wrap gap-3">
          <a href="#" className="hover:underline">개인정보처리방침</a>
          <span>|</span>
          <Link to="/" className="hover:underline">로그인</Link>
        </div>
        <div className="text-center md:text-left">
          경기도 용인시 처인구 용인대학교 134(우17092) | 대표전화 : 031-332-6471~6 | 팩스 : 031-332-6479
        </div>
        <div className="text-center md:text-right">
          Copyright(c) 2017,{" "}
          <span className="font-medium text-gray-800">Yong-in University.</span>{" "}
          All rights reserved.
        </div>
      </div>
    </footer>
  );
}
