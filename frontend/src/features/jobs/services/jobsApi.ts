import type { Job } from '../types/job'

export async function fetchJobs(signal?: AbortSignal): Promise<Job[]> {
  const response = await fetch('/api/v1/jobs', { signal })

  if (!response.ok) {
    throw new Error('Não foi possível carregar as vagas')
  }

  return response.json() as Promise<Job[]>
}
