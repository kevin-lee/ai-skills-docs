import type {ReactNode} from 'react';
import clsx from 'clsx';
import Heading from '@theme/Heading';
import styles from './styles.module.css';


type FeatureItem = {
  title: string;
  Svg: React.ComponentType<React.ComponentProps<'svg'>>;
  description: ReactNode;
};

const FeatureList: FeatureItem[] = [
  {
    title: 'For AI Agent Skills',
    Png: require('@site/static/img/ai.png').default,
    description: (
      <>
        ai-skills is a CLI tool to manage multiple AI agent skills
        (<code>install</code>, <code>list</code>, <code>read</code>, <code>update</code>, <code>sync</code>, <code>remove</code>).
      </>
    ),
  },
  {
    title: 'Support macOS and Linux',
    Png: require('@site/static/img/apple-and-linux-800x800.png').default,
    description: (
      <>
        It works on both macOS 🖥️ and Linux 🐧.
      </>
    ),
  },
  {
    title: 'Powered by Scala Native',
    Svg: require('@site/static/img/scala.svg').default,
    description: (
      <>
        Completely written in Scala Native<br/>
        So it does not require JVM or Node.js.
      </>
    ),
  },
];

function FeatureSvg({title, Svg, description}: FeatureItem) {
  return (
    <div className={clsx('col col--4')}>
      <div className="text--center">
        <Svg className={styles.featureSvg} role="img" />
      </div>
      <div className="text--center padding-horiz--md">
        <Heading as="h3">{title}</Heading>
        <p>{description}</p>
      </div>
    </div>
  );
}

function FeaturePng({Png, title, description}) {
  return (
      <div className={clsx('col col--4')}>
        <div className="text--center">
          <img src={Png} className={styles.featurePng} alt={title} />
        </div>
        <div className="text--center padding-horiz--md">
          <h3>{title}</h3>
          <p>{description}</p>
        </div>
      </div>
  );
}

export default function HomepageFeatures(): ReactNode {
  return (
    <section className={styles.features}>
      <div className="container">
        <div className="row">
          {FeatureList.map((props, idx) => (
              props.hasOwnProperty('Svg') ?
                  <FeatureSvg key={idx} {...props} /> :
                  <FeaturePng key={idx} {...props} />
          ))}
        </div>
      </div>
    </section>
  );
}

