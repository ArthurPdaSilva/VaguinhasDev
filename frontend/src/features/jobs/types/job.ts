export type Job = {
  id: string
  sourceUrl: string
  company: string
  title: string
  seniority: string
  workModel: string
  location: string
  publishedAt: string | null
}

export type JobsStatus = 'loading' | 'ready' | 'error'
