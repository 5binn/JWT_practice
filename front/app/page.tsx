'use client'

import Image from "next/image";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Home() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      console.log(document.cookie);
    }, 1000);
  }, [])

  const logout = () => {
    fetch("http://localhost:8070/api/v1/members/logout");
  }

  return (
    <main >
      <Link href={"/user/login"}>로그인</Link>
      <button onClick={logout}>로그아웃</button>
    </main>
  );
}
