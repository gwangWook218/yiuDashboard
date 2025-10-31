export default function TopNav() {
  return (
    <div className="bg-[#2F5664] text-white flex items-center px-4 py-2 shadow-md">
      {/* 왼쪽 메뉴 */}
      <div className="flex items-center gap-6 ml-4">
        <a href="/report" className="font-semibold text-sm hover:underline">
          성과지표
        </a>
        <a href="/faculty" className="font-semibold text-sm hover:underline">
          재정지표
        </a>
        <a href="/employees" className="font-semibold text-sm hover:underline">
          중도탈락
        </a>
      </div>
    </div>
  );
}
