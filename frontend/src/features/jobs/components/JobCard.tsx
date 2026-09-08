import type { Job } from '../types/job'

type JobCardProps = {
  job: Job
  index: number
}

export function JobCard({ job, index }: JobCardProps) {
  const tags = [
    ['location', job.location],
    ['workModel', job.workModel],
    ['seniority', job.seniority],
  ]

  return (
    <article className="grid grid-cols-result items-start gap-3 border-line border-t py-card last:border-b sm:grid-cols-job-wide sm:gap-7.5">
      <span className="font-mono text-count text-muted uppercase">
        {(index + 1).toString().padStart(2, '0')}
      </span>
      <div>
        <p className="mb-2 font-mono font-medium text-label text-signal uppercase">
          {job.company}
        </p>
        <h3 className="m-0 font-sans text-card text-ink">{job.title}</h3>
        <div className="mt-5 flex flex-wrap gap-2">
          {tags.map(([name, value]) => (
            <span
              className="border border-line px-2.5 py-1.5 font-mono text-tag text-muted uppercase"
              key={name}
            >
              {value.replace('_', ' ')}
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
  )
}
