const facilities = [
  {
    name: "학생회관(인성관)",
    lat: 37.228158,
    lng: 127.169899,
    info: {
      image: "/2.jpg",
      desc: "(3층) 학생지원과, 장학과, 학생생활연구소, 동문회사무실, 취업지원센터, 비상계획과",
      instagram: "https://www.instagram.com/yongin_university?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "용인대학교 인스타 : @yongin_university"
    },
    amenities: [
      { img: "/17.jpg", desc: "[1층 GS25] 월 ~ 목 07:00 ~ 20:00 / 금 07:00 ~ 17:30" },
      { img: "/18.jpg", desc: "[1층 서점] 전화 문의 031-8020-3927" },
      { img: "/19.jpg", desc: "[1층 문구 & 기념품 스포츠용품] 월 ~ 금 08:30 ~ 17:30 / 031-8020-3903" },
      { img: "/20.jpg", desc: "[1층 복사실] 월 ~ 금 08:30 ~ 18:00" },
      { img: "/21.jpg", desc: "[1층 안경점] 월 ~ 금 10:00 ~ 17:00" },
      { img: "/22.jpg", desc: "[1층 MEGA COFFEE] 학기 중 월 ~ 금 08:30 ~ 19:00" },
      { img: "/26.jpg", desc: "1층 ATM [신한은행], [KEB하나은행]" },
      { img: "/23.jpg", desc: "[3층 여학생 휴게실] 월 ~ 금 09:00 ~ 17:00 / 총학생회 031-8020-2813" },
      { img: "/24.jpg", desc: "[3층 학생지원과 팀 스터디룸] 사무실 문의 사용신청 후 이용" },
      { img: "/25.jpg", desc: "[3층 학생지원과 노트북 대여] 월 ~ 금(09:00 ~ 17:00) / 대여신청 후 이용" },
    ]
  },
  {
    name: "중앙도서관",
    lat: 37.225903,
    lng: 127.167252,
    info: {
      image: "/11.jpg",
      desc: "(2층) 학술정보열람과, (5층) 학술정보지원과",
      instagram: "https://www.instagram.com/yiu_library?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "중앙도서관 인스타 : @yiu_library"
    },
    amenities: []
  },
  {
    name: "종합체육관",
    lat: 37.226154,
    lng: 127.169315,
    info: {
      image: "/7.jpg",
      desc: "(1층) 종합 실습실 및 체육 실기장, (2층) 씨름장, 합주실, 골프수선실, (3층) 평생교육센터, (4층) 태권도실기장2"
    },
    amenities: [
      { img: "/27.jpg", desc: "[1층 검도 실기장] 면적 : 952.6㎡" },
      { img: "/28.jpg", desc: "[1층 레슬링 실기장] 면적 : 476.3㎡" },
      { img: "/29.jpg", desc: "[1층 복싱 실기장] 면적 : 476.3㎡" },
      { img: "/30.jpg", desc: "[1층 택견 실기장] 면적 : 335.2㎡" },
      { img: "/32.jpg", desc: "[1층 트레이닝장] 면적 : 141.1㎡" },
      { img: "/33.jpg", desc: "[1층 농구장] 면적 : 1,428.8㎡" },
      { img: "/31.jpg", desc: "[2층 씨름 실기장] 면적 : 423.4㎡" },
    ]
  },
  {
    name: "생활관",
    lat: 37.225323,
    lng: 127.167450,
    info: {
      image: "/12.jpg",
      desc: "학생들에게 교육적 목적을 가지고 숙식과 주거를 할 수 있는 공간"
    },
    amenities: [
      { img: "/34.jpg", desc: "[1층 GS25] 월 ~ 목(08:00 ~ 20:00) / 금 ~ 일(08:00 ~ 19:00)" },
      { img: "/26.jpg", desc: "1층 ATM [신한은행]" },
    ]
  },
  {
    name: "무도대학",
    lat: 37.228994,
    lng: 127.168035,
    info: {
      image: "/1.jpg",
      desc: "유도학과, 유도경기지도학과, 무도학과, 태권도학과, 경호학과",
      instagram: "https://www.instagram.com/yiu_haru?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "무도대학 학생회 인스타 : @yiu_haru"
    },
    amenities: [
      { img: "/23.jpg", desc: "[지하 1층 여학생 휴게실] 월 ~ 금 09:00 ~ 17:00 / 총학생회 031-8020-2813" },
      { img: "/36.jpg", desc: "[지하 1층 창의융합라운지] 월 ~ 금 09:00 ~ 17:00" },
      { img: "/35.jpg", desc: "[1층 WATCH COFFEE] 월 ~ 금 08:30 ~ 18:00" },
      { img: "/37.jpg", desc: "[1층 휴대폰 무인충전기]" },
      { img: "/48.jpg", desc: "[1층 크리에이티브 스튜디오, 스마트 스튜디오] 평일 09:00 ~ 17:00 / 강의·인터뷰·공연·유튜브 영상 등 다양한 촬영이 가능한 전문 스튜디오 공간을 제공 / 원격교육지원센터(☎ 031-8020-3599)" },
      { img: "/26.jpg", desc: "1층 ATM [신한은행]" },
      { img: "/38.jpg", desc: "[2층 용무도 실기장] 면적 : 577.6㎡" },
      { img: "/39.jpg", desc: "[2층 태권도 실기장] 면적 : 1244.1㎡" },
      { img: "/40.jpg", desc: "[4층 유도 실기장] 면적 : 유도실기장1 - 1244.1㎡, 유도실기장2 - 577.6㎡"},
      { img: "/41.jpg", desc: "[5층 트레이닝장] 면적 : 492.4㎡" }
    ]
  },
  {
    name: "체육과학대학",
    lat: 37.227649,
    lng: 127.168914,
    info: {
      image: "/3.jpg",
      desc: "스포츠레저학과, 체육학과, 골프학부, 특수체육교육과",
      instagram: "https://www.instagram.com/yiu_aura?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "체육과학대학 학생회 인스타 : @yiu_aura"
    },
    amenities: [
      { img: "/37.jpg", desc: "[1층 휴대폰 무인충전기]" },
      { img: "/44.jpg", desc: "[1층 특수체육 실기장] 면적 : 388.9㎡" },
      { img: "/45.jpg", desc: "[1층 체조 실기장] 면적 : 388.6㎡" },
      { img: "/23.jpg", desc: "[3층 여학생 휴게실] 월 ~ 금 09:00 ~ 17:00 / 총학생회 031-8020-2813" },
      { img: "/42.jpg", desc: "[4층 탁구 실기장] 면적 : 379.8㎡" },
      { img: "/43.jpg", desc: "[4층 댄스스포츠 실기장] 면적 : 379.8㎡" }
    ]
  },
  {
    name: "문화예술대학",
    lat: 37.226813,
    lng: 127.170160,
    info: {
      image: "/5.jpg",
      desc: "무용과, 미디어디자인학과, 회화학과, 연극학과, 국악과, 영화영상학과, 문화유산학과, 문화콘텐츠학과, 실용음악과",
      instagram: "https://www.instagram.com/yiu_ullim?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "문화예술대학 학생회 인스타 : @yiu_ullim"
    },
    amenities: [
      { img: "/46.jpg", desc: "[1동 1층 파리바게트] 월 ~ 금 08:30 ~ 18:00" },
      { img: "/20.jpg", desc: "[1동 1층 복사실] 월 ~ 금 08:30 ~ 18:00 / 방학 중 10:00 ~ 15:00" },
      { img: "/23.jpg", desc: "[1동 1층 여학생 휴게실] 월 ~ 금 09:00 ~ 17:00 / 총학생회 031-8020-2813" },
      { img: "/47.jpg", desc: "[1동 3층 Creative Lounge] 자유롭게 공부하고 소통하며 휴식할수 있는 공간 / 상시 개방" },
      { img: "/48.jpg", desc: "[1동 3층 크리에이티브 스튜디오, 스마트 스튜디오] 평일 09:00 ~ 17:00 / 강의·인터뷰·공연·유튜브 영상 등 다양한 촬영이 가능한 전문 스튜디오 공간을 제공 / 원격교육지원센터(☎ 031-8020-3599)" },
      { img: "/24.jpg", desc: "[1동 3층 원격교육지원센터 팀 스터디룸] 사무실 문의 사용신청 후 이용" },
      { img: "/37.jpg", desc: "[3동 4층 휴대폰 무인충전기]" },
      { img: "/26.jpg", desc: "신관 1층 ATM [신한은행]" }
    ]
  },
  {
    name: "인문사회융합대학",
    lat: 37.226236,
    lng: 127.166126,
    info: {
      image: "/10.jpg",
      desc: "경영학과, 관광경영학과, 경찰행정학과, 중국학과, 사회복지학과",
      instagram: "https://www.instagram.com/yiu_han.gyeol?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "문화예술대학 학생회 인스타 : @yiu_han.gyeol"
    },
    amenities: [
      { img: "/49.jpg", desc: "[지하 1층 편의점] 24시간 무인매점" },
      { img: "/23.jpg", desc: "[지하 1층 여학생 휴게실] 월 ~ 금 09:00 ~ 17:00 / 총학생회 031-8020-2813" },
      { img: "/37.jpg", desc: "[2층 휴대폰 무인충전기]" },
      { img: "/36.jpg", desc: "[4층 창의융합라운지] 월 ~ 금 09:00 ~ 17:00" },
      { img: "/26.jpg", desc: "1층 ATM [우체국]" }
    ]
  },
  {
    name: "AI바이오융합대학",
    lat: 37.228504,
    lng: 127.166648,
    info: {
      image: "/8.jpg",
      desc: "보건환경안전학과, 바이오생명공학과, 물리치료학과, 식품조리학부, AI융합학부",
      instagram: "https://www.instagram.com/yiu_sori?utm_source=ig_web_button_share_sheet&igsh=ZDNlZDc0MzIxNw==",
      instagramLabel: "문화예술대학 학생회 인스타 : @yiu_sori"
    },
    amenities: [
      { img: "/23.jpg", desc: "[2층 여학생 휴게실] 월 ~ 금 09:00 ~ 17:00 / 총학생회 031-8020-2813" }
    ]
  },
  {
    name: "용오름대학",
    lat: 37.227936,
    lng: 127.166274,
    info: {
      image: "/9.jpg",
      desc: "교양지원과 - 교양과목 담당교수들의 강의 및 행정지원 운영"
    },
     amenities: [
      { img: "/50.jpg", desc: "[지하 1층 GS25] 월 ~ 금 09:00 ~ 18:00" },
      { img: "/20.jpg", desc: "[지하 1층 복사실] 월 ~ 금 12:30 ~ 17:00" },
      { img: "/37.jpg", desc: "[1층 휴대폰 무인충전기]" },
      { img: "/36.jpg", desc: "[5층 창의융합라운지] 월 ~ 금 09:00 ~ 17:00" },
      { img: "/26.jpg", desc: "1층 ATM [KEB하나은행]" }
    ]
  },
  {
    name: "시니어서비스센터",
    lat: 37.228620,
    lng: 127.171356,
    info: {
      image: "/6.jpg",
      desc: "지역 어르신들의 건강하고 행복한 삶을 지원하고, 학생들의 지역사회 봉사 정신을 함양하기 위한 목적으로 운영"
    },
    amenities: []
  },
  {
    name: "골프실기장",
    lat: 37.228709,
    lng: 127.166042,
    info: {
      image: "/13.jpg",
      desc: "일반 시민들도 사용할 수 있는 골프연습장 (용인시민 월9만원)"
    },
    amenities: [
      { img: "/51.jpg", desc: "건물연면적 – 2,076.2㎡ / 페어웨이면적 – 8,640㎡" },
    ]
  },
  {
    name: "대운동장",
    lat: 37.227594,
    lng: 127.167732,
    info: {
      image: "/14.jpg",
      desc: "면적 : 49,000㎡ / 월 ~ 금(08:30 ~ 20:00) 이용문의 - 총무지원과(031-8020-2527~8)"
    },
    amenities: [
    ]
  },
  {
    name: "대학본부",
    lat: 37.227324,
    lng: 127.166260,
    info: {
      image: "/4.jpg",
      desc: "기획처, 교무처, 교육혁신처, 학생처, 사무처, 국제교류교육원, 입학관리실, 기관생명윤리위원회"
    },
    amenities: [
      {img: "/26.jpg", desc: "1층 ATM [신한은행], [KEB하나은행]" }
    ]
  },
  {
    name: "학생군사교육단",
    lat: 37.227016,
    lng: 127.165927,
    info: {
      image: "/16.jpg",
      desc: "용인대학교 학군단(ROTC)은 2006년 창설 이후 우수한 장교를 양성하며 600여 명의 장교를 배출한 대학생 군사교육 기관"
    },
    amenities: []
  },
  {
    name: "테니스장 / 소운동장",
    lat: 37.224745,
    lng: 127.168302,
    info: {
      image: "/15.jpg",
      desc: "테니스장 및 소규모 운동장."
    },
    amenities: [
      { img: "/52.jpg", desc: "[생활관 옆 테니스장] 면적 : 2,253.6㎡ " },
      { img: "/51.jpg", desc: "[생활관 옆 소운동장] 면적 : 4,385.1㎡" }
    ]
  },
  {
    name: "풋살장",
    lat: 37.228558,
    lng: 127.169623,
    info: {
      image: "/54.jpg",
      desc: "학생회관(인성관) 후면 구기전용 구장"
    },
    amenities: []
  }
];

export default facilities;
