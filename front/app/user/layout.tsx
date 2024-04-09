'use client'

import Link from "next/link";
import { useEffect } from "react";



export default function UserLayout({
    children,
}: Readonly<{
    children: React.ReactNode;
}>) {

    const handleLogout = async () => {
        const response = await fetch("http://localhost:8070/api/v1/members/logout", {
            method: "POST",
            credentials: "include", //핵심 변경점
            headers: {
                'Content-Type': "application/json"
            }
        });

        if (response.ok) {
            alert('성공');
        } else {
            alert('실패');
        }
    }

    return (
        <>
            <h1>유저페이지</h1>
            <nav>
                <Link className="nav-link" href="/">홈 |</Link>
                <Link className="nav-link" href="/article"> 게시글 |</Link>
                <Link className="nav-link" href="/about"> 소개 |</Link>
                <Link href={"/user/login"}> 로그인 |</Link>
                <button onClick={handleLogout}> 로그아웃</button>
            </nav>
            {children}
        </>
    );
}
