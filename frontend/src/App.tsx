import { useDeferredValue, useEffect, useState } from 'react'

type Job = {
  id: string
  sourceUrl: string
  company: string
  title: string
  seniority: string
  workModel: string
  location: string
  publishedAt: string | null
}

function App() {
  const [jobs, setJobs] = useState<Job[]>([])
  const [query, setQuery] = useState('')
  const [error, setError] = useState(false)
  const [loading, setLoading] = useState(true)
  const deferredQuery = useDeferredValue(
    query.trim().toLocaleLowerCase('pt-BR'),
  )

  useEffect(() => {
    const controller = new AbortController()

    fetch('/api/v1/jobs', { signal: controller.signal })
      .then((response) => {
        if (!response.ok) throw new Error('Não foi possível carregar as vagas')
        return response.json() as Promise<Job[]>
      })
      .then(setJobs)
      .catch((requestError: unknown) => {
        if (requestError instanceof Error && requestError.name !== 'AbortError')
          setError(true)
      })
      .finally(() => setLoading(false))

    return () => controller.abort()
  }, [])

  const visibleJobs = jobs.filter((job) =>
    `${job.title} ${job.company} ${job.location}`
      .toLocaleLowerCase('pt-BR')
      .includes(deferredQuery),
  )

  return (
    <div className="mx-auto max-w-content px-page-mobile sm:px-page">
      <header className="flex min-h-topbar-mobile items-center justify-between border-line border-b sm:min-h-topbar">
        <a
          className="font-bold text-brand text-ink no-underline"
          href="/"
          aria-label="DevJobs, página inicial"
        >
          dev<span className="text-signal">/</span>jobs
        </a>
        <p className="hidden font-mono text-label text-muted uppercase tracking-label sm:block">
          Vagas de tecnologia, direto da fonte.
        </p>
      </header>

      <main>
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
              onChange={(event) => setQuery(event.target.value)}
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
            {['Greenhouse', 'Lever', 'Ashby'].map((source) => (
              <li
                className="border border-line px-2.5 py-1.5 font-mono text-tag text-muted uppercase"
                key={source}
              >
                {source}
              </li>
            ))}
          </ul>
        </section>

        <section
          className="pt-section pb-section-end"
          aria-labelledby="jobs-title"
        >
          <div className="mb-7.5 flex items-baseline justify-between">
            <h2 className="font-semibold text-ink text-section" id="jobs-title">
              Vagas recentes
            </h2>
            <span className="hidden font-mono text-count text-muted uppercase sm:block">
              {visibleJobs.length.toString().padStart(2, '0')} oportunidades
            </span>
          </div>

          {loading && (
            <p className="border-line border-y py-8 font-mono text-notice text-muted">
              Buscando novas oportunidades...
            </p>
          )}
          {error && (
            <p className="border-line border-y py-8 font-mono text-notice text-danger">
              Não foi possível acessar a API. Verifique se o backend está
              rodando.
            </p>
          )}
          {!loading && !error && visibleJobs.length === 0 && (
            <div className="grid grid-cols-result gap-3 border-line border-y py-13 sm:grid-cols-result-wide sm:gap-7.5">
              <span className="font-mono font-medium text-label text-signal">
                01
              </span>
              <div>
                <h3 className="m-0 font-sans text-empty text-ink tracking-card">
                  {query
                    ? 'Nenhuma vaga encontrada'
                    : 'A coleta começa em breve'}
                </h3>
                <p className="mt-3 max-w-copy text-muted leading-6">
                  {query
                    ? 'Tente buscar por outro cargo, empresa ou local.'
                    : 'As primeiras vagas aparecerão aqui após a execução dos collectors.'}
                </p>
              </div>
            </div>
          )}

          <div>
            {visibleJobs.map((job, index) => (
              <article
                className="grid grid-cols-result items-start gap-3 border-line border-t py-card last:border-b sm:grid-cols-job-wide sm:gap-7.5"
                key={job.id}
              >
                <span className="font-mono text-count text-muted uppercase">
                  {(index + 1).toString().padStart(2, '0')}
                </span>
                <div>
                  <p className="mb-2 font-mono font-medium text-label text-signal uppercase">
                    {job.company}
                  </p>
                  <h3 className="m-0 font-sans text-card text-ink">
                    {job.title}
                  </h3>
                  <div className="mt-5 flex flex-wrap gap-2">
                    {[job.location, job.workModel, job.seniority].map((tag) => (
                      <span
                        className="border border-line px-2.5 py-1.5 font-mono text-tag text-muted uppercase"
                        key={tag}
                      >
                        {tag.replace('_', ' ')}
                      </span>
                    ))}
                  </div>
                </div>
                <a
                  className="col-start-2 mt-2.5 font-mono font-medium text-link text-ink uppercase no-underline sm:col-start-auto sm:mt-0"
                  href={job.sourceUrl}
                  target="_blank"
                  rel="noreferrer"
                >
                  Ver vaga <span className="text-signal">↗</span>
                </a>
              </article>
            ))}
          </div>
        </section>
      </main>

      <footer className="flex flex-col gap-3 border-line border-t py-7.5 sm:flex-row sm:justify-between sm:pb-10.5">
        <p className="font-mono text-label text-muted uppercase tracking-label">
          DevJobs Aggregator
        </p>
        <p className="font-mono text-label text-muted uppercase tracking-label">
          Candidaturas sempre na fonte original.
        </p>
      </footer>
    </div>
  )
}

export default App
