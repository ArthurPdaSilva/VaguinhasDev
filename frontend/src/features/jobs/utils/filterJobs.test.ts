import { describe, expect, it } from 'vitest'
import type { Job } from '../types/job'
import { filterJobs } from './filterJobs'

const jobs: Job[] = [
  {
    id: '1',
    sourceUrl: 'https://example.com/jobs/1',
    company: 'Tecnologia Ágil',
    title: 'Pessoa Desenvolvedora Java',
    seniority: 'SENIOR',
    workModel: 'REMOTE',
    location: 'São Paulo',
    publishedAt: null,
  },
]

describe('filterJobs', () => {
  it('returns every job for an empty query', () => {
    expect(filterJobs(jobs, '')).toEqual(jobs)
  })

  it('searches title, company and location ignoring case and accents', () => {
    expect(filterJobs(jobs, 'sao paulo')).toEqual(jobs)
    expect(filterJobs(jobs, 'JAVA')).toEqual(jobs)
    expect(filterJobs(jobs, 'tecnologia agil')).toEqual(jobs)
  })

  it('returns an empty list when no job matches', () => {
    expect(filterJobs(jobs, 'Kotlin')).toEqual([])
  })
})
