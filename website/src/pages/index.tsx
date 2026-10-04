import {type ReactNode, useCallback, useEffect, useRef, useState} from 'react';
import clsx from 'clsx';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import useBaseUrl from '@docusaurus/useBaseUrl';
import Layout from '@theme/Layout';
import HomepageFeatures from '@site/src/components/HomepageFeatures';
import Heading from '@theme/Heading';

import styles from './index.module.css';

const INTRO_MESSAGE = 'ai-skills-intro';

// The intro uses the Fullscreen API where the browser has it. iPhone Safari has none for pages,
// so there the intro asks through postMessage to be expanded over the viewport, which is done here.
function IntroVideo(): ReactNode {
  const frame = useRef<HTMLIFrameElement>(null);
  const [expanded, setExpanded] = useState(false);
  const expandedRef = useRef(expanded);

  const sendState = useCallback(() => {
    frame.current?.contentWindow?.postMessage(
      {type: INTRO_MESSAGE, fullscreen: expandedRef.current},
      window.location.origin,
    );
  }, []);

  useEffect(() => {
    expandedRef.current = expanded;
    sendState();
    document.documentElement.style.overflow = expanded ? 'hidden' : '';
  }, [expanded, sendState]);

  useEffect(() => {
    const onMessage = (e: MessageEvent) => {
      if (e.source !== frame.current?.contentWindow || e.data?.type !== INTRO_MESSAGE) return;
      if (typeof e.data.fullscreen === 'boolean') setExpanded(e.data.fullscreen);
      else sendState();
    };
    const onKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') setExpanded(false);
    };
    window.addEventListener('message', onMessage);
    window.addEventListener('keydown', onKeyDown);
    return () => {
      window.removeEventListener('message', onMessage);
      window.removeEventListener('keydown', onKeyDown);
      document.documentElement.style.overflow = '';
    };
  }, [sendState]);

  return (
    <div className={clsx(styles.intro, expanded && styles.introExpanded)}>
      <iframe
        ref={frame}
        src={useBaseUrl('/intro/ai-skills-intro.html')}
        title="ai-skills intro video"
        loading="lazy"
        allow="autoplay; fullscreen"
        allowFullScreen
        onLoad={sendState}
      />
    </div>
  );
}

function HomepageHeader() {
  const {siteConfig} = useDocusaurusContext();
  return (
    <header className={clsx('hero hero--primary', styles.heroBanner)}>
      <div className="container">
        <img
          src="https://hits.sh/github.com/kevin-lee/ai-skills.svg"
          style={{ display: 'none' }}
          alt="hit counter"
        />
        <img className={styles.logo} src={`../../img/ai-skills-all.svg`} alt="Project Logo"/>
        <IntroVideo />
        <Heading as="h1" className="hero__title">
          {siteConfig.title}
        </Heading>
        <p className="hero__subtitle">{siteConfig.tagline}</p>
        <div className={styles.buttons}>
          <Link
            className="button button--secondary button--lg"
            to="/docs">
            Getting Started
          </Link>
        </div>
      </div>
    </header>
  );
}

export default function Home(): ReactNode {
  const {siteConfig} = useDocusaurusContext();
  return (
    <Layout
      title={`Hello from ${siteConfig.title}`}
      description="Description will go into a meta tag in <head />">
      <HomepageHeader />
      <main>
        <HomepageFeatures />
      </main>
    </Layout>
  );
}
