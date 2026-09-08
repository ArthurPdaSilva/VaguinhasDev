import type { Job } from '../types/job'

function normalize(value: string) {
  return value
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .toLocaleLowerCase('pt-BR')
    .trim()
}

export function filterJobs(jobs: Job[], query: string) {
  const normalizedQuery = normalize(query)

  if (!normalizedQuery) return jobs

  return jobs.filter((job) =>
    normalize(`${job.title} ${job.company} ${job.location}`).includes(
      normalizedQuery,
    ),
  )
}
