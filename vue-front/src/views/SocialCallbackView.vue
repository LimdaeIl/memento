<template>
  <div>구글 로그인 처리중...</div>
</template>

<script setup>
import { onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();

onMounted(async () => {
  const code = route.query.code;
  if (!code) return alert("code가 없습니다.");

  // 중복 호출 방지(새로고침/재진입 방어)
  const usedKey = `social_exchange_used:${code}`;
  if (sessionStorage.getItem(usedKey)) {
    return router.replace("/"); // 이미 처리한 code면 홈으로
  }
  sessionStorage.setItem(usedKey, "1");

  const res = await fetch("http://localhost:30000/api/v1/auth/social/exchange", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ code }),
  });

  const json = await res.json();

  const accessToken = json?.data?.accessToken;
  const refreshToken = json?.data?.refreshToken;

  if (!res.ok || !accessToken || !refreshToken) {
    console.log("exchange 실패 응답:", res.status, json);
    alert("토큰 교환 실패");
    return;
  }

  localStorage.setItem("accessToken", accessToken);
  localStorage.setItem("refreshToken", refreshToken);

  // ✅ URL에 code 남기지 않기
  router.replace("/");
});
</script>
