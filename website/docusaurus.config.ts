import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

import LatestVersion from '@types/commonTypes';

const algoliaConfig = require('./algolia.config.json');
const googleAnalyticsConfig = require('./google-analytics.config.json');

const lightCodeTheme = prismThemes.nightOwlLight;
const darkCodeTheme = prismThemes.nightOwl;

const isEmptyObject = (obj: object) => Object.keys(obj).length === 0;

const isSearchable = !isEmptyObject(algoliaConfig);
const hasGoogleAnalytics = !isEmptyObject(googleAnalyticsConfig);

import LatestVersionImported from './latestVersion.json';
const latestVersionFound = LatestVersionImported as LatestVersion;

const gtag = hasGoogleAnalytics ? { 'gtag': googleAnalyticsConfig } : null;



// This runs in Node.js - Don't use client-side code here (browser APIs, JSX...)

const config: Config = {
  title: 'AI Skills',
  tagline: 'A tool to manage AI agent skills',
  favicon: 'img/ai-skills-logo-only.svg',

  // Future flags, see https://docusaurus.io/docs/api/docusaurus-config#future
  future: {
    v4: true, // Improve compatibility with the upcoming Docusaurus v4
  },

  // Set the production url of your site here
  url: 'https://ai-skills.kevinly.dev',
  // Set the /<baseUrl>/ pathname under which your site is served
  // For GitHub pages deployment, it is often '/<projectName>/'
  baseUrl: '/',

  // GitHub pages deployment config.
  // If you aren't using GitHub pages, you don't need these.
  organizationName: 'kevin-lee', // Usually your GitHub org/user name.
  projectName: 'ai-skills', // Usually your repo name.

  onBrokenLinks: 'throw',

  // Even if you don't use internationalization, you can use this field to set
  // useful metadata like html lang. For example, if your site is Chinese, you
  // may want to replace "en" with "zh-Hans".
  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          path: '../generated-docs/docs/',
          sidebarPath: require.resolve('./sidebars.js'),
          "lastVersion": "current",
          "versions": {
            "current": {
              "label": `v${latestVersionFound.version}`,
            },
          }
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
        ...gtag,
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    // Replace with your project's social card
    image: 'img/ai-skills-all.svg',
    colorMode: {
      respectPrefersColorScheme: true,
    },
    docs: {
      sidebar: {
        hideable: true,
      },
    },
    navbar: {
      title: 'AI Skills',
      logo: {
        alt: 'AI Skills Logo',
        src: 'img/ai-skills-logo-only.svg',
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docsSidebar',
          position: 'left',
          label: 'Docs',
        },
        {
          href: 'https://github.com/kevin-lee/ai-skills',
          label: 'GitHub',
          position: 'right',
        },
        {
          type: 'docsVersionDropdown',
          position: 'right',
          dropdownActiveClassDisabled: true,
          dropdownItemsAfter: [
            {
              to: '/versions',
              label: 'All versions',
            },
          ],
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Getting Started',
              to: '/docs',
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: 'https://github.com/kevin-lee/ai-skills',
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} ai-skills is designed and developed by <a href="https://github.com/kevin-lee" target="_blank">Kevin Lee</a>.<br>The website built with Docusaurus.
      <div><a href="https://www.flaticon.com/free-icons/artificial-intelligence" title="artificial intelligence icons">AI</a>, <a href="https://www.flaticon.com/free-icons/scala" title="scala icons">Scala</a> icons created by Freepik - Flaticon</div>
      `,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.dracula,
    },
  } satisfies Preset.ThemeConfig,
};

if (isSearchable) {
  config['themeConfig']['algolia'] = algoliaConfig;
}

export default config;
