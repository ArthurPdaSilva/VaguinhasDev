import { useEffect, useState } from 'react'
import { fetchJobs } from '../services/jobsApi'
import type { Job, JobsStatus } from '../types/job'

export function useJobs() {
  const [jobs, setJobs] = useState<Job[]>([])
  const [status, setStatus] = useState<JobsStatus>('loading')

  useEffect(() => {
    const controller = new AbortController()

    fetchJobs(controller.signal)
      .then((loadedJobs) => {
        setJobs(loadedJobs)
        setStatus('ready')
      })
      .catch((error: unknown) => {
        if (error instanceof Error && error.name !== 'AbortError') {
          setStatus('error')
        }
      })

    return () => controller.abort()
  }, [])

  return { jobs, status }
}
