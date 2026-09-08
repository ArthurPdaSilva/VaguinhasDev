type JobsHeroProps = {
  query: string
  onQueryChange: (query: string) => void
}

const sources = ['Greenhouse', 'Lever', 'Ashby']

export function JobsHero({ query, onQueryChange }: JobsHeroProps) {
  return (
    <section className="border-line border-b py-hero-mobile sm:py-hero">
      <p className="mb-6 font-mono font-medium text-label text-signal uppercase tracking-label">
        Oportunidades selecionadas de ATS públicos
      </p>
      <h1 className="max-w-display font-semibold text-display text-ink">
        Seu próximo trabalho
        <br />
        pode estar aqui.
      </h1>
      <label className="mt-10 grid max-w-search grid-cols-search items-center gap-3.5 border border-ink bg-paper py-2.5 pr-2.5 pl-3.5 font-mono font-medium text-search-label uppercase shadow-search sm:mt-search-top sm:grid-cols-search-wide sm:gap-6 sm:pl-5.5">
        <span className="text-ink">Buscar</span>
        <input
          className="min-w-0 border-0 bg-transparent py-3 font-sans text-base text-ink outline-0 placeholder:text-placeholder"
          type="search"
          value={query}
          onChange={(event) => onQueryChange(event.target.value)}
          placeholder="Cargo, empresa ou localização"
        />
        <kbd className="hidden bg-ink px-3 py-2.5 font-mono text-paper sm:block">
          ⌘ K
        </kbd>
      </label>
      <ul
        className="mt-7 flex list-none flex-wrap gap-2.5 p-0"
        aria-label="Fontes de vagas"
      >
        {sources.map((source) => (
          <li
            className="border border-line px-2.5 py-1.5 font-mono text-tag text-muted uppercase"
            key={source}
          >
            {source}
          </li>
        ))}
      </ul>
    </section>
  )
}
