export default function buildQueryString(
  currentParams: URLSearchParams,
  nextValues: Record<string, string | number | null | undefined>
) {
  const updated = new URLSearchParams(currentParams.toString())

  Object.entries(nextValues).forEach(([key, value]) => {
    if (value === null || value === undefined || value === '') {
      updated.delete(key)
      return
    }

    updated.set(key, String(value))
  })

  return updated.toString()
}
