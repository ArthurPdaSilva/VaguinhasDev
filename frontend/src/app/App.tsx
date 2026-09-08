import { useDeferredValue, useState } from 'react'
import { JobsHero } from '../features/jobs/components/JobsHero'
import { JobsSection } from '../features/jobs/components/JobsSection'
import { useJobs } from '../features/jobs/hooks/useJobs'
import { filterJobs } from '../features/jobs/utils/filterJobs'
import { SiteFooter } from '../shared/components/SiteFooter'
import { SiteHeader } from '../shared/components/SiteHeader'

export function App() {
  const [query, setQuery] = useState('')
  const deferredQuery = useDeferredValue(query)
  const { jobs, status } = useJobs()
  const visibleJobs = filterJobs(jobs, deferredQuery)

  return (
    <div className="mx-auto max-w-content px-page-mobile sm:px-page">
      <SiteHeader />
      <main>
        <JobsHero query={query} onQueryChange={setQuery} />
        <JobsSection
          jobs={visibleJobs}
          status={status}
          hasQuery={query.trim().length > 0}
        />
      </main>
      <SiteFooter />
    </div>
  )
}
