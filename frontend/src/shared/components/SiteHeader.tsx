export function SiteHeader() {
  return (
    <header className="flex min-h-topbar-mobile items-center justify-between border-line border-b sm:min-h-topbar">
      <a
        className="font-bold text-brand text-ink no-underline"
        href="/"
        aria-label="VaguinhasDev, página inicial"
      >
        vaguinhas<span className="text-signal">/</span>dev
      </a>
      <p className="hidden font-mono text-label text-muted uppercase tracking-label sm:block">
        Vagas de tecnologia, direto da fonte.
      </p>
    </header>
  )
}
