import type { Job, JobsStatus } from '../types/job'
import { JobCard } from './JobCard'

type JobsSectionProps = {
  jobs: Job[]
  status: JobsStatus
  hasQuery: boolean
}

export function JobsSection({ jobs, status, hasQuery }: JobsSectionProps) {
  return (
    <section className="pt-section pb-section-end" aria-labelledby="jobs-title">
      <div className="mb-7.5 flex items-baseline justify-between">
        <h2 className="font-semibold text-ink text-section" id="jobs-title">
          Vagas recentes
        </h2>
        <span className="hidden font-mono text-count text-muted uppercase sm:block">
          {jobs.length.toString().padStart(2, '0')} oportunidades
        </span>
      </div>

      {status === 'loading' && (
        <p className="border-line border-y py-8 font-mono text-notice text-muted">
          Buscando novas oportunidades...
        </p>
      )}
      {status === 'error' && (
        <p className="border-line border-y py-8 font-mono text-notice text-danger">
          Não foi possível acessar a API. Verifique se o backend está rodando.
        </p>
      )}
      {status === 'ready' && jobs.length === 0 && (
        <div className="grid grid-cols-result gap-3 border-line border-y py-13 sm:grid-cols-result-wide sm:gap-7.5">
          <span className="font-mono font-medium text-label text-signal">
            01
          </span>
          <div>
            <h3 className="m-0 font-sans text-empty text-ink tracking-card">
              {hasQuery
                ? 'Nenhuma vaga encontrada'
                : 'A coleta começa em breve'}
            </h3>
            <p className="mt-3 max-w-copy text-muted leading-6">
              {hasQuery
                ? 'Tente buscar por outro cargo, empresa ou local.'
                : 'As primeiras vagas aparecerão aqui após a execução dos collectors.'}
            </p>
          </div>
        </div>
      )}

      <div>
        {jobs.map((job, index) => (
          <JobCard job={job} index={index} key={job.id} />
        ))}
      </div>
    </section>
  )
}
