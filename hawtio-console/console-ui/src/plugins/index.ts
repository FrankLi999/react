import { HawtioPlugin } from '@hawtio/react'
import { registerRefreshConfig } from './refresh-config'

export const registerMyPlugins: HawtioPlugin = () => {
    registerRefreshConfig()
  
}
