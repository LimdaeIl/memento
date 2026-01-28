<template>
  <div style="padding: 24px;">
    <h2>memento - Social Login Test</h2>

    <button @click="loginWithGoogle" style="padding: 10px 14px; margin-top: 12px;">
      Google로 로그인
    </button>

    <div style="margin-top: 20px;">
      <h3>토큰 상태</h3>
      <pre>{{ tokenState }}</pre>
      <button @click="clearTokens" style="padding: 8px 12px;">토큰 삭제</button>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";

const API_BASE = import.meta.env.VITE_API_BASE_URL;

function loginWithGoogle() {
  // 백엔드가 OAuth 플로우 시작
  window.location.href = `${API_BASE}/api/v1/auth/social/google`;
}

function clearTokens() {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  window.location.reload();
}

const tokenState = computed(() => {
  return {
    accessToken: localStorage.getItem("accessToken"),
    refreshToken: localStorage.getItem("refreshToken"),
  };
});
</script>
