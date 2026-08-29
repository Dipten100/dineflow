'use client'

// React Imports
import type { CSSProperties } from 'react'

// Third-party Imports
import styled from '@emotion/styled'

// Config Imports
import themeConfig from '@configs/themeConfig'
import Image from 'next/image'

type LogoTextProps = {
  color?: CSSProperties['color']
}

const LogoText = styled.span<LogoTextProps>`
  color: ${({ color }) => color ?? 'var(--mui-palette-text-primary)'};
  font-size: 1.25rem;
  line-height: 1.2;
  font-weight: 600;
  letter-spacing: 0.15px;
  text-transform: uppercase;
  margin-inline-start: 10px;
`

const Logo = ({ color }: { color?: CSSProperties['color'] }) => {
  return (
    <div className='flex items-center min-bs-[24px]'>
      <Image src={`/images/logos/DineFlow_icon_${themeConfig.mode}.svg`} alt='DineFlow Logo' width={48} height={48} />
      <LogoText color={color}>{themeConfig.templateName}</LogoText>
    </div>
  )
}

export default Logo
