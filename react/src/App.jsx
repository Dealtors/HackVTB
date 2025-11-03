import React, { useMemo, useState } from "react";

export default function App() {
  const [scene, setScene] = useState("welcome");
  const [loginLoading, setLoginLoading] = useState(false);
  const [reg, setReg] = useState({
    name: "",
    nick: "",
    email: "",
    pass: "",
    pass2: "",
    role: "tester",
    consent: false,
  });
  const [err, setErr] = useState({});

  // Стили — теперь аккуратно и без белой тени
  const css = useMemo(() => `
:root{
  --brand:#2f3bd7;
  --brand-2:#5a6bff;
  --text:#0a0a0a;
  --muted:#9aa3b2;
  --bg:#eef3ff;
  --card:#ffffff;
  --shadow:0 20px 60px rgba(22,27,70,.18);
  --radius:24px;
  --radius-lg:28px;
  --input:#f2f3f7;
  --danger:#e03131;
}
*{box-sizing:border-box}
html,body,#root{height:100%}
body{
  margin:0;
  font-family:Inter,system-ui,-apple-system,Segoe UI,Roboto,Arial,"Noto Sans",sans-serif;
  color:var(--text);
  background:var(--bg);
}

.logo{position:fixed;top:2vw;left:2vw;z-index:5}
.logo-img{width:clamp(96px,8vw,208px);height:auto;display:block}

.wrapper{position:relative;min-height:100vh;overflow:hidden}
 {
  po.scenesition: absolute;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  padding-top: 40px; 
  overflow-y: auto;
  transition: opacity .35s ease, transform .35s ease;
  opacity: 0;
  pointer-events: none;
  transform: translateY(12px);
}
  .scene {
  position: absolute;
  inset: 0;
  display: flex;
  justify-content: center;
  align-items: center;  /* было flex-start — теперь строго по центру */
  padding: 0;
  overflow: hidden;
  transition: opacity .35s ease, transform .35s ease;
  opacity: 0;
  pointer-events: none;
  transform: translateY(12px);
}
.scene.active {
  opacity: 1;
  pointer-events: auto;
  transform: translateY(0);
}
.bg-welcome{
  background:linear-gradient(180deg,#f8fbff 0%,#e9f0ff 40%,#c7d6ff 80%,#a4b5ff 100%);
}
.bg-welcome:before{
  content:"";position:absolute;inset:0;
  background:repeating-linear-gradient(90deg,rgba(255,255,255,.35) 0,rgba(255,255,255,.35) 2px,rgba(255,255,255,0) 2px,rgba(255,255,255,0) 6px);
  pointer-events:none;
}
.bg-welcome:after{
  content:"";position:absolute;inset:0;
  background:radial-gradient(1000px 420px at 50% 100%,rgba(28,43,140,.45),transparent 70%);
  pointer-events:none;
}

.bg-auth{
  background:radial-gradient(900px 420px at 20% 70%,rgba(47,59,215,.35),transparent 60%),
              radial-gradient(680px 320px at 78% 24%,rgba(47,59,215,.25),transparent 60%),
              linear-gradient(180deg,#f7faff 0%,#eaf0ff 55%,#cad6ff 100%);
}

.welcome-card{
  position:relative;
  z-index:1;
  display:grid;
  gap:24px;
  place-items:center;
  text-align:center;
  padding:160px 24px 64px;
  color:white;
}

.welcome-title{
  font-size:38px;
  line-height:1.2;
  font-weight:800;
  color:#fff;
  text-shadow:0 2px 10px rgba(0,0,60,0.5);
}

.welcome-sub{
  max-width:760px;
  font-size:16px;
  opacity:.95;
  text-shadow:0 2px 12px rgba(0,0,0,.45);
  margin-top:20px;
  color:#fff;
}

.btn{
  appearance:none;
  border:0;
  border-radius:999px;
  padding:12px 28px;
  font-weight:700;
  font-size:15px;
  background:white;
  color:#1a2add;
  box-shadow:0 12px 30px rgba(14,22,78,.28);
  cursor:pointer;
  transition:transform .08s ease,filter .2s ease;
  position:relative;
  z-index:2;
  margin-top:20px;
}
.btn:hover{filter:brightness(1.05)}
.btn:active{transform:translateY(1px)}
.btn[disabled]{opacity:.7;cursor:not-allowed}

.card{
  position:relative;
  z-index:2;
  background:var(--card);
  border-radius:var(--radius-lg);
  box-shadow:var(--shadow);
  width:min(92vw,420px);
  padding:28px;
}
.card h1{margin:0 0 18px;font-size:22px;text-align:center}

.form{display:grid;gap:14px}
.input{
  width:100%;
  border:0;
  background:var(--input);
  padding:14px;
  border-radius:12px;
  font-size:15px;
  outline:none;
}
.input:focus{box-shadow:0 0 0 2px rgba(47,59,215,.18)}
.input.error{box-shadow:0 0 0 2px var(--danger) inset}
.submit{width:100%;margin-top:10px;background:var(--brand);color:#fff}

.muted{color:var(--muted);font-size:12px;text-align:center;margin-top:8px}
.muted a{color:var(--brand);text-decoration:none;font-weight:600}

.roles{display:flex;align-items:center;gap:18px;margin:8px 0 2px}
.roles label{display:flex;align-items:center;gap:8px;font-size:14px;color:#1a2540}

.consent{display:flex;align-items:flex-start;gap:10px;font-size:12px;color:#626b7a;margin-top:3px}
.error-text{font-size:12px;color:var(--danger);margin-top:-6px;min-height:16px}

.blob{position:absolute;border-radius:50%;filter:blur(24px);opacity:.6;pointer-events:none}
.blob.one{width:520px;height:520px;left:8%;bottom:-12%;background:radial-gradient(circle at 40% 40%,#8da0ff,#5b6cff)}
.blob.two{width:320px;height:320px;right:12%;top:14%;background:radial-gradient(circle at 40% 40%,#9eb0ff,#6e7bff)}

.footer-gradient{
  position:fixed;
  inset:0;
  background:linear-gradient(180deg,transparent 0%, rgba(14,22,78,.0) 45%, rgba(14,22,78,.85) 100%);
  pointer-events:none;
  z-index:0;
}

.loading{margin-top:15px;text-align:center;font-size:14px;color:#333;display:none}
.loading.show{display:block}
`, []);

  const go = (k) => {
    setScene(k);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  function handleLoginSubmit(e) {
    e.preventDefault();
    setLoginLoading(true);
    setTimeout(() => setLoginLoading(false), 2000);
  }

  function validateRegistration(values) {
    const e = {};
    const emailRe = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
    if (!values.name || values.name.trim().length < 2) e.name = "Укажите имя (мин. 2 символа).";
    if (!values.nick || values.nick.trim().length < 2) e.nick = "Укажите ник (мин. 2 символа).";
    if (!emailRe.test(values.email?.trim())) e.email = "Неверный формат email.";
    if (!values.pass || values.pass.length < 6) e.pass = "Пароль короче 6 символов.";
    if (values.pass2 !== values.pass) e.pass2 = "Пароли не совпадают.";
    if (!values.role) e.role = "Выберите роль.";
    if (!values.consent) e.consent = "Нужно согласиться с обработкой данных.";
    return e;
  }

  function submitRegister(e) {
    e.preventDefault();
    const errors = validateRegistration(reg);
    setErr(errors);
    if (Object.keys(errors).length) return;
    const btn = e.currentTarget;
    btn.disabled = true;
    setTimeout(() => {
      btn.disabled = false;
      go("login");
    }, 1000);
  }

  const setField = (k, v) => setReg((p) => ({ ...p, [k]: v }));

  return (
    <div className="wrapper">
      <style dangerouslySetInnerHTML={{ __html: css }} />

      <div className="logo" aria-label="ВТБ">
        <img src="/vtb-logo.png" alt="ВТБ" className="logo-img" />
      </div>

      {/* WELCOME */}
      <section className={`scene bg-welcome ${scene === "welcome" ? "active" : ""}`}>
        <div className="welcome-card">
          <div className="footer-gradient" />
          <h1 className="welcome-title">Добро пожаловать в Оркестр API.</h1>
          <p className="welcome-sub">
            Ваш проводник от идеи до безупречно работающего процесса!
            <br />Мы поможем упростить вашу работу и сделаем её максимально комфортной!
          </p>
          <button className="btn" onClick={() => go("login")}>ВОЙТИ</button>
        </div>
      </section>

      {/* LOGIN */}
      <section className={`scene bg-auth ${scene === "login" ? "active" : ""}`}>
        <div className="blob one" />
        <div className="blob two" />
        <div className="card">
          <h1>Вход</h1>
          <form className="form" onSubmit={handleLoginSubmit}>
            <input className="input" type="email" placeholder="Email" required />
            <input className="input" type="password" placeholder="Пароль" required />
            <button className="btn submit" disabled={loginLoading}>
              {loginLoading ? "Подождите..." : "Войти"}
            </button>
            <div className={`loading ${loginLoading ? "show" : ""}`}>Проверяем данные...</div>
          </form>
          <div className="muted">
            Нет аккаунта?{" "}
            <a href="#" onClick={(e) => { e.preventDefault(); go("register"); }}>Зарегистрироваться</a>
          </div>
        </div>
      </section>

      {/* REGISTER */}
      <section className={`scene bg-auth ${scene === "register" ? "active" : ""}`}>
        <div className="blob one" />
        <div className="blob two" />
        <div className="card">
          <h1>Регистрация</h1>
          <form className="form" onSubmit={submitRegister}>
            <input
              className={`input ${err.name ? "error" : ""}`}
              type="text"
              placeholder="Имя"
              value={reg.name}
              onChange={(e) => { setField("name", e.target.value); setErr((p) => ({ ...p, name: undefined })); }}
              required
            />
            <div className="error-text">{err.name}</div>

            <input
              className={`input ${err.nick ? "error" : ""}`}
              type="text"
              placeholder="Отображаемый ник"
              value={reg.nick}
              onChange={(e) => { setField("nick", e.target.value); setErr((p) => ({ ...p, nick: undefined })); }}
              required
            />
            <div className="error-text">{err.nick}</div>

            <input
              className={`input ${err.email ? "error" : ""}`}
              type="email"
              placeholder="Email"
              value={reg.email}
              onChange={(e) => { setField("email", e.target.value); setErr((p) => ({ ...p, email: undefined })); }}
              required
            />
            <div className="error-text">{err.email}</div>

            <input
              className={`input ${err.pass ? "error" : ""}`}
              type="password"
              placeholder="Пароль"
              value={reg.pass}
              onChange={(e) => { setField("pass", e.target.value); setErr((p) => ({ ...p, pass: undefined })); }}
              required
            />
            <div className="error-text">{err.pass}</div>

            <input
              className={`input ${err.pass2 ? "error" : ""}`}
              type="password"
              placeholder="Подтверждение пароля"
              value={reg.pass2}
              onChange={(e) => { setField("pass2", e.target.value); setErr((p) => ({ ...p, pass2: undefined })); }}
              required
            />
            <div className="error-text">{err.pass2}</div>

            <div className="roles">
              <span>Роль:</span>
              <label>
                <input
                  type="radio"
                  name="role"
                  value="tester"
                  checked={reg.role === "tester"}
                  onChange={(e) => setField("role", e.target.value)}
                />
                Тестировщик
              </label>
              <label>
                <input
                  type="radio"
                  name="role"
                  value="analyst"
                  checked={reg.role === "analyst"}
                  onChange={(e) => setField("role", e.target.value)}
                />
                Аналитик
              </label>
            </div>
            <div className="error-text">{err.role}</div>

            <button className="btn submit" type="submit">Зарегистрироваться</button>
            <label className="consent">
              <input
                type="checkbox"
                checked={reg.consent}
                onChange={(e) => setField("consent", e.target.checked)}
              />
              <span>Я соглашаюсь на обработку персональных данных</span>
            </label>
            <div className="error-text">{err.consent}</div>
          </form>
          <div className="muted">
            Уже есть аккаунт?{" "}
            <a href="#" onClick={(e) => { e.preventDefault(); go("login"); }}>Войти</a>
          </div>
        </div>
      </section>
    </div>
  );
}
